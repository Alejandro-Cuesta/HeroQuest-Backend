package dev.alejandro.heroquest_backend.inventory;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.alejandro.heroquest_backend.inventory.controller.InventoryController;
import dev.alejandro.heroquest_backend.inventory.dto.ItemDTORequest;
import dev.alejandro.heroquest_backend.inventory.dto.ItemDTOResponse;
import dev.alejandro.heroquest_backend.inventory.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InventoryController.class)
class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventoryService inventoryService;

    @Autowired
    private ObjectMapper objectMapper;

    private Authentication authentication;

    @BeforeEach
    void setUp() {
        authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testuser");
    }

    @Test
    void addItem_ShouldReturnOk() throws Exception {
        ItemDTORequest dto = new ItemDTORequest();
        dto.setName("Sword");
        dto.setType("Weapon");
        dto.setBonusAttack(5);

        ItemDTOResponse response = ItemDTOResponse.builder()
                .id(1L).name("Sword").type("Weapon").bonusAttack(5).build();

        when(inventoryService.addItemToHero(authentication, dto)).thenReturn(response);

        mockMvc.perform(post("/api/inventory/add")
                .principal(() -> "testuser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Sword"))
                .andExpect(jsonPath("$.bonusAttack").value(5));
    }

    @Test
    void getItems_ShouldReturnOk() throws Exception {
        ItemDTOResponse item1 = ItemDTOResponse.builder().id(1L).name("Sword").type("Weapon").bonusAttack(5).build();
        ItemDTOResponse item2 = ItemDTOResponse.builder().id(2L).name("Shield").type("Armor").bonusDefense(3).build();

        when(inventoryService.getItemsOfHero(authentication)).thenReturn(List.of(item1, item2));

        mockMvc.perform(get("/api/inventory")
                .principal(() -> "testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Sword"))
                .andExpect(jsonPath("$[1].name").value("Shield"));
    }

    @Test
    void removeItem_ShouldReturnNoContent() throws Exception {
        Long itemId = 1L;
        doNothing().when(inventoryService).removeItemFromHero(authentication, itemId);

        mockMvc.perform(delete("/api/inventory/item/{id}", itemId)
                .principal(() -> "testuser"))
                .andExpect(status().isNoContent());

        verify(inventoryService).removeItemFromHero(authentication, itemId);
    }
}