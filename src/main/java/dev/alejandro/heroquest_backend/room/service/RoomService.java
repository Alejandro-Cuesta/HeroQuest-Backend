package dev.alejandro.heroquest_backend.room.service;

import dev.alejandro.heroquest_backend.hero.model.Hero;
import dev.alejandro.heroquest_backend.room.model.GameSession;
import dev.alejandro.heroquest_backend.room.model.Room;

import java.util.List;

public interface RoomService {
    List<Room> getAllRooms();
    GameSession enterRoom(Hero hero, Long roomId);
    GameSession getSessionByHero(Hero hero);
}