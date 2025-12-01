package com.fahmi.chatappapi.dto.request;

import lombok.Data;

@Data
public class MessageRequest {
    private String roomId;
    private String content;
    private String receiver;
    private String mediaUrl;
}
