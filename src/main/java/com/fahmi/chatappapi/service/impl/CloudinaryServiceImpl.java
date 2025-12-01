package com.fahmi.chatappapi.service.impl;

import com.cloudinary.Cloudinary;
import com.fahmi.chatappapi.dto.response.UploadMediaResponse;
import com.fahmi.chatappapi.exception.CustomException;
import com.fahmi.chatappapi.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;

    @Override
    public UploadMediaResponse uploadMedia(MultipartFile file, String folderName) {
        try {
            Map<?, ?> result = cloudinary.uploader().upload(
                    file.getBytes(), Map.of("folder", folderName));

            return UploadMediaResponse.builder()
                    .mediaUrl(result.get("secure_url").toString())
                    .build();
        } catch (IOException e) {
            throw new CustomException.ConflictException("Failed to upload file.");
        }
    }
}
