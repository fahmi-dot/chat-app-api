package com.fahmi.chatappapi.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MessageResponse {
    private String id;
    private String content;
    private LocalDateTime sentAt;
    private String senderId;
}
