package com.fahmi.chatappapi.repository;

import com.fahmi.chatappapi.entity.Message;
import com.fahmi.chatappapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, String> {
    List<Message> findByRoomIdOrderBySentAtDesc(String roomId);

    Message findTopByRoomIdOrderBySentAtDesc(String roomId);

    Integer countByRoomIdAndSenderNotAndIsReadFalse(String roomId, User sender);

    List<Message> findByRoomIdAndSenderNotAndIsReadFalse(String roomId, User sender);
}
