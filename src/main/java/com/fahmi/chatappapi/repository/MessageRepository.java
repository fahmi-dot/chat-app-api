package com.fahmi.chatappapi.repository;

import com.fahmi.chatappapi.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, String> {
}
