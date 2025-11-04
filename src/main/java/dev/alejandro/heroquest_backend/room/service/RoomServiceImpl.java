package dev.alejandro.heroquest_backend.room.service;

import dev.alejandro.heroquest_backend.hero.model.Hero;
import dev.alejandro.heroquest_backend.room.model.GameSession;
import dev.alejandro.heroquest_backend.room.model.Room;
import dev.alejandro.heroquest_backend.room.repository.GameSessionRepository;
import dev.alejandro.heroquest_backend.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final GameSessionRepository sessionRepository;

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public GameSession enterRoom(Hero hero, Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        // Si ya existe una sesión para este héroe, actualizar
        GameSession session = sessionRepository.findByHero(hero)
                .orElse(GameSession.builder().hero(hero).startedAt(LocalDateTime.now()).build());

        session.setRoom(room);
        return sessionRepository.save(session);
    }

    @Override
    public GameSession getSessionByHero(Hero hero) {
        return sessionRepository.findByHero(hero)
                .orElseThrow(() -> new IllegalStateException("No active game session for this hero"));
    }
}