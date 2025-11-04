package dev.alejandro.heroquest_backend.room.repository;

import dev.alejandro.heroquest_backend.room.model.GameSession;
import dev.alejandro.heroquest_backend.hero.model.Hero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameSessionRepository extends JpaRepository<GameSession, Long> {
    Optional<GameSession> findByHero(Hero hero);
}