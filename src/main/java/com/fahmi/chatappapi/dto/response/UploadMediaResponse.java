package com.fahmi.chatappapi.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UploadMediaResponse {
    private String mediaUrl;
}
