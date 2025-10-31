package dev.alejandro.heroquest_backend.inventory.service;

import dev.alejandro.heroquest_backend.auth.model.User;
import dev.alejandro.heroquest_backend.auth.repository.UserRepository;
import dev.alejandro.heroquest_backend.hero.model.Hero;
import dev.alejandro.heroquest_backend.hero.repository.HeroRepository;
import dev.alejandro.heroquest_backend.inventory.dto.ItemDTORequest;
import dev.alejandro.heroquest_backend.inventory.dto.ItemDTOResponse;
import dev.alejandro.heroquest_backend.inventory.mapper.ItemMapper;
import dev.alejandro.heroquest_backend.inventory.model.Inventory;
import dev.alejandro.heroquest_backend.inventory.model.Item;
import dev.alejandro.heroquest_backend.inventory.repository.InventoryRepository;
import dev.alejandro.heroquest_backend.inventory.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de inventario.
 * - Cada héroe tiene un inventario único.
 * - Permite añadir, listar y eliminar ítems persistentes.
 * - El héroe se resuelve automáticamente a partir del usuario autenticado (Authentication).
 */
@Service
@RequiredArgsConstructor
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final HeroRepository heroRepository;
    private final UserRepository userRepository;

    /**
     * Método auxiliar para obtener el héroe del usuario autenticado.
     * Usa el username del objeto Authentication para evitar errores de referencia entre entidades.
     * @param authentication objeto con la información del usuario logueado.
     * @return el héroe asociado al usuario autenticado.
     */
    private Hero getHeroFromAuth(Authentication authentication) {
        String username = authentication.getName();

        // Verificamos que el usuario exista en la base de datos
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User not found: " + username));

        // Buscamos el héroe asociado a este usuario
        return heroRepository.findByUser(user)
                .orElseThrow(() -> new IllegalStateException("This user has no hero assigned"));
    }

    /**
     * Añadir un ítem al inventario del héroe autenticado.
     * Si el héroe no tiene inventario, se crea uno nuevo automáticamente.
     */
    @Override
    public ItemDTOResponse addItemToHero(Authentication authentication, ItemDTORequest dto) {
        Hero hero = getHeroFromAuth(authentication);

        // Buscar o crear inventario para el héroe
        Inventory inventory = inventoryRepository.findByHero(hero)
                .orElseGet(() -> {
                    Inventory newInventory = Inventory.builder().hero(hero).build();
                    return inventoryRepository.save(newInventory);
                });

        // Convertimos el DTO en entidad Item y lo vinculamos al inventario
        Item item = itemMapper.toItem(dto);
        item.setInventory(inventory);

        // Guardamos el ítem en la base de datos
        Item savedItem = itemRepository.save(item);

        // Devolvemos el DTO de respuesta
        return itemMapper.toItemDTOResponse(savedItem);
    }

    /**
     * Listar todos los ítems del inventario del héroe autenticado.
     */
    @Override
    public List<ItemDTOResponse> getItemsOfHero(Authentication authentication) {
        Hero hero = getHeroFromAuth(authentication);

        return inventoryRepository.findByHero(hero)
                .map(inventory -> inventory.getItems().stream()
                        .map(itemMapper::toItemDTOResponse)
                        .collect(Collectors.toList()))
                .orElse(List.of());
    }

    /**
     * Eliminar un ítem específico del inventario del héroe autenticado.
     * - Verifica que el ítem exista.
     * - Verifica que el ítem pertenezca al inventario del héroe.
     */
    @Override
    public void removeItemFromHero(Authentication authentication, Long itemId) {
        Hero hero = getHeroFromAuth(authentication);

        Inventory inventory = inventoryRepository.findByHero(hero)
                .orElseThrow(() -> new IllegalStateException("Hero has no inventory"));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalStateException("Item not found"));

        if (!item.getInventory().equals(inventory)) {
            throw new IllegalStateException("Item does not belong to this hero");
        }

        itemRepository.delete(item);
    }
}