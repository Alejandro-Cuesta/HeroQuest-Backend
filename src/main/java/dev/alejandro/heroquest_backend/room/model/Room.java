package dev.alejandro.heroquest_backend.room.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rooms")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;  // Nombre de la room (en castellano para UI)

    @Column
    private String image; // URL de imagen

    @Column(length = 500)
    private String description; // Descripción breve de la room
}