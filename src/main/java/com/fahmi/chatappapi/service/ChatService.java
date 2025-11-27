package com.fahmi.chatappapi.service;

import com.fahmi.chatappapi.dto.request.MessageRequest;
import com.fahmi.chatappapi.dto.response.MessageResponse;
import com.fahmi.chatappapi.dto.response.RoomResponse;

import java.util.List;

public interface ChatService {
    List<RoomResponse> getChatRooms();


    MessageResponse sendChatMessage(String roomId, String content, String currentUsername);

    List<MessageResponse> getChatMessages(String roomId);

    void markAsRead(String roomId);

    void deleteChatMessage(String messageId);

    String getRoomId(String currentUsername, String targetUsername);

    String createRoom(String currentUsername, String targetUsername);
}
