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
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
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

    @PostMapping("/start")
    public ResponseEntity<?> startChat(@RequestParam String username) {
        RoomResponse response = chatService.startChat(username);

        return ResponseUtil.response(HttpStatus.OK, "Chat started.", response);
    }

    @GetMapping("/{roomId}/messages")
    public ResponseEntity<?> getMessages(@PathVariable String roomId) {
        List<MessageResponse> responses = chatService.getMessages(roomId);

        return ResponseUtil.response(HttpStatus.OK, "All messages retrieved successfully.", responses);
    }

    @MessageMapping("/chat/{roomId}")
    public void sendMessage(@DestinationVariable String roomId, MessageRequest request, Principal principal) {
        MessageResponse response = chatService.sendMessage(roomId, principal.getName(), request);

        messagingTemplate.convertAndSend("/topic/messages/" + roomId, response);
    }
}
