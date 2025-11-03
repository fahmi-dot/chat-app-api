package com.fahmi.chatappapi.service;

import com.fahmi.chatappapi.dto.request.MessageRequest;
import com.fahmi.chatappapi.dto.response.MessageResponse;
import com.fahmi.chatappapi.dto.response.RoomResponse;

import java.util.List;

public interface ChatService {
    List<RoomResponse> getChatList();

    String getRoomId(String targetUsername);

    String createRoom(String targetUsername);

    List<MessageResponse> getMessages(String roomId);

    MessageResponse sendMessage(String roomId, String usnSender, MessageRequest request);
}
