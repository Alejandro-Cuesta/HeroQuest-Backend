package dev.alejandro.heroquest_backend.inventory;

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
import dev.alejandro.heroquest_backend.inventory.service.InventoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InventoryServiceImplTest {

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemMapper itemMapper;

    @Mock
    private HeroRepository heroRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    private User user;
    private Hero hero;
    private Inventory inventory;
    private Item item;
    private ItemDTORequest dto;
    private ItemDTOResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = User.builder().id(1L).username("testuser").build();
        hero = Hero.builder().id(1L).heroClass("Barbarian").user(user).build();
        inventory = Inventory.builder().id(1L).hero(hero).build();
        item = Item.builder().id(1L).name("Sword").type("Weapon").inventory(inventory).build();
        dto = new ItemDTORequest();
        dto.setName("Sword");
        dto.setType("Weapon");
        dto.setBonusAttack(5);
        dto.setBonusDefense(0);
        dto.setBonusHealth(0);
        response = ItemDTOResponse.builder().id(1L).name("Sword").type("Weapon").bonusAttack(5).bonusDefense(0).bonusHealth(0).build();

        when(authentication.getName()).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(heroRepository.findByUser(user)).thenReturn(Optional.of(hero));
    }

    @Test
    void addItemToHero_ShouldCreateInventoryIfNotExistsAndReturnDTO() {
        when(inventoryRepository.findByHero(hero)).thenReturn(Optional.empty());
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(inventory);
        when(itemMapper.toItem(dto)).thenReturn(item);
        when(itemRepository.save(item)).thenReturn(item);
        when(itemMapper.toItemDTOResponse(item)).thenReturn(response);

        ItemDTOResponse result = inventoryService.addItemToHero(authentication, dto);

        assertNotNull(result);
        assertEquals("Sword", result.getName());
        verify(itemRepository).save(item);
    }

    @Test
    void getItemsOfHero_ShouldReturnListOfDTOs() {
        inventory.setItems(List.of(item));
        when(inventoryRepository.findByHero(hero)).thenReturn(Optional.of(inventory));
        when(itemMapper.toItemDTOResponse(item)).thenReturn(response);

        List<ItemDTOResponse> items = inventoryService.getItemsOfHero(authentication);

        assertEquals(1, items.size());
        assertEquals("Sword", items.get(0).getName());
    }

    @Test
    void removeItemFromHero_ShouldDeleteItemSuccessfully() {
        inventory.setItems(List.of(item));
        when(inventoryRepository.findByHero(hero)).thenReturn(Optional.of(inventory));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        inventoryService.removeItemFromHero(authentication, 1L);

        verify(itemRepository).delete(item);
    }

    @Test
    void removeItemFromHero_ShouldThrowIfItemNotBelongToHero() {
        Inventory otherInventory = Inventory.builder().id(2L).hero(hero).build();
        item.setInventory(otherInventory);
        when(inventoryRepository.findByHero(hero)).thenReturn(Optional.of(inventory));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> inventoryService.removeItemFromHero(authentication, 1L));
        assertEquals("Item does not belong to this hero", ex.getMessage());
    }
}
