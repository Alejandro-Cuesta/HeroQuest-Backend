package dev.alejandro.heroquest_backend.room.service;

import dev.alejandro.heroquest_backend.room.model.Room;
import dev.alejandro.heroquest_backend.room.repository.RoomRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader {

    private final RoomRepository roomRepository;

    @PostConstruct
    public void loadRooms() {
        if(roomRepository.count() == 0) {
            roomRepository.save(Room.builder()
                    .name("Mazmorra de las Sombras")
                    .build());
            roomRepository.save(Room.builder()
                    .name("Bosque de los Secretos")
                    .build());
            roomRepository.save(Room.builder()
                    .name("Castillo de la Perdición")
                    .build());
        }
    }
}