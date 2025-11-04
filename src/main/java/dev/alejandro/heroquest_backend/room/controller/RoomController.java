package dev.alejandro.heroquest_backend.room.controller;

import dev.alejandro.heroquest_backend.hero.model.Hero;
import dev.alejandro.heroquest_backend.hero.repository.HeroRepository;
import dev.alejandro.heroquest_backend.room.model.GameSession;
import dev.alejandro.heroquest_backend.room.model.Room;
import dev.alejandro.heroquest_backend.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;
    private final HeroRepository heroRepository;

    @GetMapping
    public ResponseEntity<List<Room>> getRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    @PostMapping("/enter/{roomId}")
    public ResponseEntity<GameSession> enterRoom(@AuthenticationPrincipal Jwt jwt, @PathVariable Long roomId) {
        String username = jwt.getSubject();
        Hero hero = heroRepository.findByUser_Username(username)
                .orElseThrow(() -> new RuntimeException("Hero not found"));

        GameSession session = roomService.enterRoom(hero, roomId);
        return ResponseEntity.ok(session);
    }

    @GetMapping("/session")
    public ResponseEntity<GameSession> getSession(@AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getSubject();
        Hero hero = heroRepository.findByUser_Username(username)
                .orElseThrow(() -> new RuntimeException("Hero not found"));

        GameSession session = roomService.getSessionByHero(hero);
        return ResponseEntity.ok(session);
    }
}