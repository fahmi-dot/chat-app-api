package com.fahmi.chatappapi.service.impl;

import com.fahmi.chatappapi.dto.request.MessageRequest;
import com.fahmi.chatappapi.dto.response.MessageResponse;
import com.fahmi.chatappapi.dto.response.RoomResponse;
import com.fahmi.chatappapi.dto.response.UploadMediaResponse;
import com.fahmi.chatappapi.entity.Message;
import com.fahmi.chatappapi.entity.Room;
import com.fahmi.chatappapi.entity.User;
import com.fahmi.chatappapi.exception.CustomException;
import com.fahmi.chatappapi.mapper.MessageMapper;
import com.fahmi.chatappapi.mapper.RoomMapper;
import com.fahmi.chatappapi.repository.MessageRepository;
import com.fahmi.chatappapi.repository.RoomRepository;
import com.fahmi.chatappapi.service.ChatService;
import com.fahmi.chatappapi.service.CloudinaryService;
import com.fahmi.chatappapi.service.UserService;
import com.fahmi.chatappapi.util.TokenHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final MessageRepository messageRepository;
    private final RoomRepository roomRepository;
    private final CloudinaryService cloudinaryService;
    private final UserService userService;
    private final TokenHolder tokenHolder;

    @Override
    public List<RoomResponse> getChatRooms() {
        String currentId = tokenHolder.getId();
        User currentUser = userService.findById(currentId);
        List<Room> rooms = roomRepository.findByParticipantsContaining(currentUser);

        return rooms.stream().map(room -> {
            Message lastMessage = messageRepository.findTopByRoomIdOrderBySentAtDesc(room.getId());
            Integer unreadMessagesCount = messageRepository.countByRoomIdAndSenderNotAndIsReadFalse(room.getId(), currentUser);
            RoomResponse response = RoomMapper.toResponse(room);
            if (lastMessage != null) {
                response.setLastMessage(lastMessage.getContent());
                response.setLastMessageSentAt(lastMessage.getSentAt());
            }

            response.setUnreadMessagesCount(unreadMessagesCount);

            response.setParticipants(response.getParticipants().stream()
                    .filter((p) -> !p.getId().equals(currentId))
                    .toList());
            return response;
        }).toList();
    }

    @Override
    public List<RoomResponse> searchChatRooms(String query) {
        String currentId = tokenHolder.getId();
        User currentUser = userService.findById(currentId);
        List<Room> rooms = roomRepository.findByParticipantsContaining(currentUser);

        return rooms.stream().filter(room ->
                room.getParticipants().stream()
                        .anyMatch(p -> p.getUsername().contains(query))
                )
                .map(RoomMapper::toResponse)
                .toList();
    }

    @Override
    public UploadMediaResponse uploadMedia(MultipartFile file) {
        String folderName = "hello_media";

        return cloudinaryService.uploadMedia(file, folderName);
    }

    @Override
    public MessageResponse sendMessage(String roomId, MessageRequest request, String currentUsername) {
        User currentUser = userService.findByUsername(currentUsername);
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("Room not found."));
        Message message = Message.builder()
                .content(request.getContent())
                .mediaUrl(request.getMediaUrl())
                .sentAt(LocalDateTime.now())
                .isRead(false)
                .room(room)
                .sender(currentUser)
                .build();
        messageRepository.save(message);

        return MessageMapper.toResponse(message);
    }

    @Override
    public List<MessageResponse> getRoomMessages(String roomId) {
        return messageRepository.findByRoomIdOrderBySentAtDesc(roomId).stream()
                .map(MessageMapper::toResponse)
                .toList();
    }

    @Override
    public void markAsRead(String roomId) {
        String id = tokenHolder.getId();
        User currentUser = userService.findById(id);

        messageRepository.findByRoomIdAndSenderNotAndIsReadFalse(roomId, currentUser).stream()
                .peek(m -> m.setRead(true))
                .forEach(messageRepository::save);
    }

    @Override
    public void deleteRoomMessage(String messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("Message not found."));

        messageRepository.delete(message);
    }

    @Override
    public String getRoomId(String currentUsername, String targetUsername) {
        User currentUser = userService.findByUsername(currentUsername);
        User targetUser = userService.findByUsername(targetUsername);
        String roomKey = generateRoomKey(currentUser.getPhoneNumber(), targetUser.getPhoneNumber());

        Room room = roomRepository.findByRoomKey(roomKey).orElse(null);

        return (room != null) ? room.getId() : null;
    }

    @Override
    public String createRoom(String currentUsername, String targetUsername) {
        User currentUser = userService.findByUsername(currentUsername);
        User targetUser = userService.findByUsername(targetUsername);
        String roomKey = generateRoomKey(currentUser.getPhoneNumber(), targetUser.getPhoneNumber());

        Room newRoom = Room.builder()
                .roomKey(roomKey)
                .participants(List.of(currentUser, targetUser))
                .build();
        roomRepository.save(newRoom);

        return newRoom.getId();
    }

    public String generateRoomKey(String phoneNumber1, String phoneNumber2) {
        List<String> sorted = Stream.of(phoneNumber1, phoneNumber2).sorted().toList();

        return sorted.get(0) + "_" + sorted.get(1);
    }
}
