package com.fahmi.chatappapi.service;

import com.fahmi.chatappapi.dto.request.MessageRequest;
import com.fahmi.chatappapi.dto.response.MessageResponse;
import com.fahmi.chatappapi.dto.response.RoomResponse;
import com.fahmi.chatappapi.dto.response.UploadMediaResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ChatService {
    List<RoomResponse> getChatRooms();

    UploadMediaResponse uploadMedia(MultipartFile file);

    MessageResponse sendMessage(String roomId, MessageRequest request, String currentUsername);

    List<MessageResponse> getRoomMessages(String roomId);

    void markAsRead(String roomId);

    void deleteRoomMessage(String messageId);

    String getRoomId(String currentUsername, String targetUsername);

    String createRoom(String currentUsername, String targetUsername);
}
