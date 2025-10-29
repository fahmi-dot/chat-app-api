package com.fahmi.chatappapi.dto.response;

import com.fahmi.chatappapi.entity.User;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RoomResponse {
    private String id;
    private List<User> participants;
}
