package dev.alejandro.heroquest_backend.inventory.service;

import dev.alejandro.heroquest_backend.inventory.dto.ItemDTORequest;
import dev.alejandro.heroquest_backend.inventory.dto.ItemDTOResponse;
import org.springframework.security.core.Authentication;

import java.util.List;

/**
 * Define las operaciones disponibles para gestionar el inventario de un héroe.
 * - Usamos Authentication para identificar al usuario logueado (y su héroe).
 * - Esto evita pasar manualmente el objeto Hero desde el controlador.
 */
public interface InventoryService {

    /**
     * Añade un nuevo ítem al inventario del héroe autenticado.
     * @param authentication información de autenticación actual (username)
     * @param dto datos del ítem a añadir
     * @return DTO del ítem añadido
     */
    ItemDTOResponse addItemToHero(Authentication authentication, ItemDTORequest dto);

    /**
     * Obtiene todos los ítems del héroe autenticado.
     * @param authentication 
     * @return lista de ítems del héroe
     */
    List<ItemDTOResponse> getItemsOfHero(Authentication authentication);

    /**
     * Elimina un ítem específico del inventario del héroe autenticado.
     * @param authentication 
     * @param itemId ID del ítem a eliminar
     */
    void removeItemFromHero(Authentication authentication, Long itemId);
}