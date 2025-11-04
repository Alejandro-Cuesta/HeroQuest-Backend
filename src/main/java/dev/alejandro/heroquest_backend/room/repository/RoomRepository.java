package dev.alejandro.heroquest_backend.room.repository;

import dev.alejandro.heroquest_backend.room.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {}
