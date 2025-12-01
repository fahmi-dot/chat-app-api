package com.fahmi.chatappapi.service;

import com.fahmi.chatappapi.dto.response.UploadMediaResponse;
import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
    UploadMediaResponse uploadMedia(MultipartFile file, String folderName);
}
