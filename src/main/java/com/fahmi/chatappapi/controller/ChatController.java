package com.fahmi.chatappapi.controller;

import com.fahmi.chatappapi.constant.Endpoint;
import com.fahmi.chatappapi.dto.request.MessageRequest;
import com.fahmi.chatappapi.dto.response.MessageResponse;
import com.fahmi.chatappapi.dto.response.RoomResponse;
import com.fahmi.chatappapi.service.ChatService;
import com.fahmi.chatappapi.service.UserService;
import com.fahmi.chatappapi.util.ResponseUtil;
import com.fahmi.chatappapi.util.TokenHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(Endpoint.CHAT)
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;
    private final TokenHolder tokenHolder;
    private final UserService userService;

    @GetMapping("/rooms")
    public ResponseEntity<?> getChatRooms() {
        List<RoomResponse> response = chatService.getChatRooms();

        return ResponseUtil.response(HttpStatus.OK, "Chat list retrieved successfully.", response);
    }

    @PostMapping("/send")
    public void sendChatMessage(@RequestBody MessageRequest request) {
        String id = tokenHolder.getId();
        String username = userService.findById(id).getUsername();
        String roomId = request.getRoomId();

        MessageResponse response;
        if (roomId == null || roomId.isEmpty()) {
            roomId = chatService.getRoomId(username, request.getReceiver());
            if (roomId == null) {
                roomId = chatService.createRoom(username, request.getReceiver());
                response = chatService.sendChatMessage(roomId, request.getContent(), username);
                response.setType("new_room");

                messagingTemplate.convertAndSendToUser(request.getReceiver(), "/queue/notifications", response);
                messagingTemplate.convertAndSendToUser(username, "/queue/notifications", response);
                return;
            }
        }

        response = chatService.sendChatMessage(roomId, request.getContent(), username);
        response.setType("new_message");

        messagingTemplate.convertAndSendToUser(request.getReceiver(), "/queue/notifications", response);
        messagingTemplate.convertAndSendToUser(username, "/queue/notifications", response);
    }

    @MessageMapping("/chat/send")
    public void sendChatMessage(@Payload MessageRequest request, @Header("simpUser") Principal principal) {
        String roomId = request.getRoomId();
        MessageResponse response;

        if (roomId == null || roomId.isEmpty()) {
            roomId = chatService.getRoomId(principal.getName(), request.getReceiver());
            if (roomId == null) {
                roomId = chatService.createRoom(principal.getName(), request.getReceiver());
                response = chatService.sendChatMessage(roomId, request.getContent(), principal.getName());
                response.setType("new_room");

                messagingTemplate.convertAndSendToUser(request.getReceiver(), "/queue/notifications", response);
                messagingTemplate.convertAndSendToUser(principal.getName(), "/queue/notifications", response);
                return;
            }
        }

        response = chatService.sendChatMessage(roomId, request.getContent(), principal.getName());
        response.setType("new_message");

        messagingTemplate.convertAndSendToUser(request.getReceiver(), "/queue/notifications", response);
        messagingTemplate.convertAndSendToUser(principal.getName(), "/queue/notifications", response);
    }

    @GetMapping("/rooms/{roomId}/messages")
    public ResponseEntity<?> getChatMessages(@PathVariable String roomId) {
        List<MessageResponse> responses = chatService.getChatMessages(roomId);

        return ResponseUtil.response(HttpStatus.OK, "Chat messages retrieved successfully.", responses);
    }

    @PatchMapping("/rooms/{roomId}/messages")
    public ResponseEntity<?> markAsRead(@PathVariable String roomId) {
        chatService.markAsRead(roomId);

        return ResponseUtil.response(HttpStatus.OK, "Chat messages marked as read successfully.", HttpStatus.OK);
    }

    @DeleteMapping("/rooms/messages/{messageId}")
    public ResponseEntity<?> deleteChatMessage(@PathVariable String messageId) {
        chatService.deleteChatMessage(messageId);

        return ResponseUtil.response(HttpStatus.OK, "Chat message deleted successfully.", HttpStatus.OK);
    }
}
