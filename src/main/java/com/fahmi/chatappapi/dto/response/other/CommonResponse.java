package com.fahmi.chatappapi.dto.response.other;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T> {
    private int status;
    private String message;
    private T data;
}
