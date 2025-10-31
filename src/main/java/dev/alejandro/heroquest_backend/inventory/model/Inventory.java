package dev.alejandro.heroquest_backend.inventory.model;

import dev.alejandro.heroquest_backend.hero.model.Hero;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa el inventario único de un héroe.
 * Contiene la lista de ítems que el héroe posee.
 */
@Entity
@Table(name = "inventories")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación 1:1 con Hero (cada héroe tiene un solo inventario)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hero_id", nullable = false, unique = true)
    private Hero hero;

    // Relación 1:N con Item (un inventario puede tener muchos ítems)
    @OneToMany(mappedBy = "inventory", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Item> items = new ArrayList<>();
}