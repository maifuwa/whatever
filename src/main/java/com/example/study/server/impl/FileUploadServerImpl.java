package com.example.study.server.impl;

import com.example.study.pojo.dto.file.FileCache;
import com.example.study.repository.file.FileCacheRepository;
import com.example.study.server.FileUploadServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

@Service
public class FileUploadServerImpl implements FileUploadServer {

    @Value("${Spring.servlet.multipart.avatar-path}")
    String avatarPath;

    @Autowired
    FileCacheRepository fileCacheRepository;

    @Override
    public String uploadAvatar(MultipartFile file) {
        return this.imageFile(file, avatarPath);
    }
    @Override
    public String imageFile(MultipartFile file, String filePath) {
        if (file.isEmpty()) {
            return null;
        }
        try {
            byte[] fileBytes = file.getBytes();
            String fileMd5 = this.fileToMd5(fileBytes);
            if (this.isExistFile(fileMd5)) {
                FileCache cache = fileCacheRepository.findFileCacheByFileMd5(fileMd5);
                return cache.getFilename();
            }

            String fileType = Objects.requireNonNull(file.getContentType()).split("/")[1];
            String fileName = fileMd5 + "." + fileType;

            Path path = Paths.get(filePath).resolve(Paths.get(fileName)).normalize().toAbsolutePath();
            Files.write(path, fileBytes);

            fileName = this.parsePath("images", filePath) + fileName;
            this.addFileMd5InCache(fileMd5, fileName);

            return fileName;
        }catch (IOException e) {
            return null;
        }
    }
    private String fileToMd5(byte[] fileBytes) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(fileBytes);
            return new BigInteger(1, md5.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isExistFile(String fileMd5) {
        return fileCacheRepository.existsFileCacheByFileMd5(fileMd5);
    }

    private void addFileMd5InCache(String fileMd5, String filename) {
        fileCacheRepository.save(new FileCache(fileMd5, filename));
    }

    private String parsePath(String type, String path) {
        return switch (type) {
            case "images" -> path.substring(path.indexOf("/images")) + "/";
            default -> "/";
        };
    }
}
