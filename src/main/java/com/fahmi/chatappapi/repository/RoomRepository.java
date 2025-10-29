package com.fahmi.chatappapi.repository;

import com.fahmi.chatappapi.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, String> {
    Optional<Room> findByRoomKey(String roomKey);
}
