package com.fahmi.chatappapi.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RoomResponse {
    private String id;
    private List<UserResponse> participants;
}
