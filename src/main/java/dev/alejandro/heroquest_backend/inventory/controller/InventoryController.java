package dev.alejandro.heroquest_backend.inventory.controller;

import dev.alejandro.heroquest_backend.inventory.dto.ItemDTORequest;
import dev.alejandro.heroquest_backend.inventory.dto.ItemDTOResponse;
import dev.alejandro.heroquest_backend.inventory.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * InventoryController:
 * Controlador REST para gestionar el inventario del héroe autenticado.
 * 
 * - Permite añadir, listar y eliminar ítems del héroe logueado.
 * - El héroe se resuelve automáticamente a partir del usuario autenticado (Authentication).
 */
@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    /**
     * Añadir un nuevo ítem al inventario del héroe autenticado.
     */
    @PostMapping("/add")
    public ResponseEntity<ItemDTOResponse> addItem(
            Authentication authentication,
            @Valid @RequestBody ItemDTORequest dto) {
        ItemDTOResponse added = inventoryService.addItemToHero(authentication, dto);
        return ResponseEntity.ok(added);
    }

    /**
     * Obtener todos los ítems del héroe autenticado.
     */
    @GetMapping
    public ResponseEntity<List<ItemDTOResponse>> getItems(Authentication authentication) {
        return ResponseEntity.ok(inventoryService.getItemsOfHero(authentication));
    }

    /**
     * Eliminar un ítem del inventario del héroe autenticado.
     */
    @DeleteMapping("/item/{id}")
    public ResponseEntity<Void> removeItem(
            Authentication authentication,
            @PathVariable Long id) {
        inventoryService.removeItemFromHero(authentication, id);
        return ResponseEntity.noContent().build();
    }
}