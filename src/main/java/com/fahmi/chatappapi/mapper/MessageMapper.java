package com.fahmi.chatappapi.mapper;

import com.fahmi.chatappapi.dto.response.MessageResponse;
import com.fahmi.chatappapi.entity.Message;

public class MessageMapper {
    public static MessageResponse toResponse(Message message) {
        return MessageResponse.builder()
                .id(message.getId())
                .content(message.getContent())
                .sentAt(message.getSentAt())
                .senderId(message.getSender().getId())
                .build();
    }
}
