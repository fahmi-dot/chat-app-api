package com.fahmi.chatappapi.mapper;

import com.fahmi.chatappapi.dto.response.RoomResponse;
import com.fahmi.chatappapi.entity.Room;

public class RoomMapper {
    public static RoomResponse toResponse(Room room) {
        return RoomResponse.builder()
                .id(room.getId())
                .participants(room.getParticipants())
                .build();
    }
}
