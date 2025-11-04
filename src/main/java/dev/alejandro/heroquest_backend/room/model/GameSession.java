package dev.alejandro.heroquest_backend.room.model;

import dev.alejandro.heroquest_backend.hero.model.Hero;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "game_sessions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hero_id", nullable = false, unique = true)
    private Hero hero;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(nullable = false)
    private LocalDateTime startedAt;

    @Column
    private String state; // JSON o string con el estado actual de la partida
}