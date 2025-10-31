package dev.alejandro.heroquest_backend.inventory.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Representa un objeto persistente que puede estar en el inventario de un héroe.
 * Puede ser un arma, armadura, poción u otro tipo de objeto.
 */
@Entity
@Table(name = "items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nombre visible del objeto
    @Column(nullable = false)
    private String name;

    // Tipo general (Weapon, Armor, Potion...)
    @Column(nullable = false)
    private String type;

    // Bonos que otorga el ítem (0 si no aplica)
    @Column(nullable = false)
    @Builder.Default
    private int bonusAttack = 0;

    @Column(nullable = false)
    @Builder.Default
    private int bonusDefense = 0;

    @Column(nullable = false)
    @Builder.Default
    private int bonusHealth = 0;

    // Muchos ítems pueden pertenecer a un mismo inventario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;
}
