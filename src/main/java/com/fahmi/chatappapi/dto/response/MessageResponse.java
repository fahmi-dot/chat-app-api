package com.fahmi.chatappapi.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MessageResponse {
    private String id;
    private String roomId;
    private String content;
    private boolean isRead;
    private LocalDateTime sentAt;
    private String senderId;
    private String type;
}
