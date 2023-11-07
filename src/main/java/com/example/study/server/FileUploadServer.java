package com.example.study.server;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadServer {

    String uploadAvatar(MultipartFile file);
    String imageFile(MultipartFile file, String filePath);
    boolean isExistFile(String fileMd5);
}
