package com.fahmi.chatappapi.controller;

import com.fahmi.chatappapi.constant.Endpoint;
import com.fahmi.chatappapi.dto.request.MessageRequest;
import com.fahmi.chatappapi.dto.response.MessageResponse;
import com.fahmi.chatappapi.dto.response.RoomResponse;
import com.fahmi.chatappapi.service.ChatService;
import com.fahmi.chatappapi.util.ResponseUtil;
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

    /*@PostMapping("/start")
    public ResponseEntity<?> startChat(@RequestParam String username) {
        String response = chatService.createRoom(username);

        return ResponseUtil.response(HttpStatus.OK, "Chat started.", response);
    }*/

    @GetMapping("/rooms")
    public ResponseEntity<?> getChatRooms() {
        List<RoomResponse> response = chatService.getChatRooms();

        return ResponseUtil.response(HttpStatus.OK, "Chat list retrieved successfully.", response);
    }

    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<?> getChatRoomDetail(@PathVariable String roomId) {
        RoomResponse response = chatService.getChatRoomDetail(roomId);

        return ResponseUtil.response(HttpStatus.OK, "Chat room detail retrieved successfully.", response);
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
                messagingTemplate.convertAndSend("/topic/messages/" + roomId, response);
                return;
            }
        }

        response = chatService.sendChatMessage(roomId, request.getContent(), principal.getName());
        response.setType("new_message");
        messagingTemplate.convertAndSend("/topic/messages/" + roomId, response);
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
