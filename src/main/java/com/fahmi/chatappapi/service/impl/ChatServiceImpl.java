package com.fahmi.chatappapi.service.impl;

import com.fahmi.chatappapi.dto.request.MessageRequest;
import com.fahmi.chatappapi.dto.response.MessageResponse;
import com.fahmi.chatappapi.dto.response.RoomResponse;
import com.fahmi.chatappapi.entity.Message;
import com.fahmi.chatappapi.entity.Room;
import com.fahmi.chatappapi.entity.User;
import com.fahmi.chatappapi.mapper.MessageMapper;
import com.fahmi.chatappapi.mapper.RoomMapper;
import com.fahmi.chatappapi.repository.MessageRepository;
import com.fahmi.chatappapi.repository.RoomRepository;
import com.fahmi.chatappapi.service.ChatService;
import com.fahmi.chatappapi.service.UserService;
import com.fahmi.chatappapi.util.TokenHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final MessageRepository messageRepository;
    private final RoomRepository roomRepository;
    private final UserService userService;
    private final TokenHolder tokenHolder;

    @Override
    public RoomResponse startChat(String targetUsername) {
        String currentUsername = tokenHolder.getUsername();
        User currentUser = userService.findByUsername(currentUsername);
        User targetUser = userService.findByUsername(targetUsername);
        String roomKey = generateRoomKey(currentUser.getPhoneNumber(), targetUser.getPhoneNumber());
        Room room = roomRepository.findByRoomKey(roomKey)
                .orElseGet(() -> {
                    Room newRoom = new Room();
                    newRoom.setRoomKey(roomKey);
                    newRoom.setParticipants(List.of(currentUser, targetUser));
                    return roomRepository.save(newRoom);
                });

        return RoomMapper.toResponse(room);
    }

    @Override
    public List<MessageResponse> getMessages(String roomId) {
        return messageRepository.findAll().stream()
                .filter((m) -> m.getRoom().getId().equals(roomId))
                .map(MessageMapper::toResponse)
                .toList();
    }

    @Override
    public MessageResponse sendMessage(String roomId, MessageRequest request) {
        String username = tokenHolder.getUsername();
        User sender = userService.findByUsername(username);
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found."));
        Message message = Message.builder()
                .content(request.getContent())
                .sentAt(LocalDateTime.now())
                .room(room)
                .sender(sender)
                .build();
        messageRepository.save(message);

        return MessageMapper.toResponse(message);
    }

    public String generateRoomKey(String phoneNumber1, String phoneNumber2) {
        List<String> sorted = Stream.of(phoneNumber1, phoneNumber2)
                .sorted()
                .toList();

        return sorted.get(0) + "_" + sorted.get(1);
    }
}
