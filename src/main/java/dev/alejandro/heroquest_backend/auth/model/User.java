package dev.alejandro.heroquest_backend.auth.model;

import dev.alejandro.heroquest_backend.hero.model.Hero;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

/**
 * Entidad User
 * Representa a un usuario registrado en la aplicación.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String displayName;

    @Column(nullable = false)
    private String password;

    /**
     * Relación 1:1 con Hero.
     * Cada usuario tiene exactamente un héroe.
     */
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Hero hero;
}