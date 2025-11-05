package com.fahmi.chatappapi.service;

import com.fahmi.chatappapi.dto.request.MessageRequest;
import com.fahmi.chatappapi.dto.response.MessageResponse;
import com.fahmi.chatappapi.dto.response.RoomResponse;

import java.util.List;

public interface ChatService {
    List<RoomResponse> getChatRooms();

    RoomResponse getChatRoomDetail(String roomId);

    List<MessageResponse> getChatMessages(String roomId);

    MessageResponse sendMessage(String roomId, String content);

    String getRoomId(String targetUsername);

    String createRoom(String targetUsername);
}
