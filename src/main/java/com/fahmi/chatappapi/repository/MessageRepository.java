package com.fahmi.chatappapi.repository;

import com.fahmi.chatappapi.entity.Message;
import com.fahmi.chatappapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, String> {
    Message findTopByRoomIdOrderBySentAtDesc(String roomId);

    Integer countByRoomIdAndSenderAndIsReadFalse(String roomId, User sender);
}
