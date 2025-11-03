package com.fahmi.chatappapi.dto.request;

import lombok.Data;

@Data
public class MessageRequest {
    private String roomId;
    private String receiver;
    private String content;
}
