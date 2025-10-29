package com.fahmi.chatappapi.util;

import com.fahmi.chatappapi.dto.response.other.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {
    public static <T>ResponseEntity<CommonResponse<T>> response(HttpStatus status, String message, T data) {
        CommonResponse<T> commonResponse = new CommonResponse<>();

        commonResponse.setStatus(status.value());
        commonResponse.setMessage(message);
        commonResponse.setData(data);

        return ResponseEntity.status(status).body(commonResponse);
    }
}
