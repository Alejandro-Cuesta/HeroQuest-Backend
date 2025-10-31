package dev.alejandro.heroquest_backend.hero.model;

import dev.alejandro.heroquest_backend.auth.model.User;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad Hero
 * Representa la clase de héroe elegida por un usuario.
 * Relación 1:1 con User (cada usuario tiene exactamente un héroe).
 */
@Entity
@Table(name = "heroes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Hero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Clase del héroe: "Barbarian", "Wizard", "Elf", "Dwarf"...
    @Column(nullable = false, unique = true)
    private String heroClass;

    // Estadísticas básicas
    @Column(nullable = false)
    @Builder.Default
    private int health = 10;

    @Column(nullable = false)
    @Builder.Default
    private int attack = 10;

    @Column(nullable = false)
    @Builder.Default
    private int defense = 10;

    @Column(nullable = false)
    @Builder.Default
    private int movement = 10;

    @Column(nullable = false)
    @Builder.Default
    private int experiencia = 0;

    @Column(nullable = false)
    @Builder.Default
    private int nivel = 1;
    
    @Column(nullable = false)
    @Builder.Default
    private int puntosRestantes = 15;

    /**
     * Relación 1:1 con User.
     * Cada héroe pertenece a exactamente un usuario.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
}
