package com.example.imageservice.controller;

import com.example.imageservice.service.MinIOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("upload")
public class UploadController {
    @Autowired
    private MinIOService minioService;

    @PostMapping()
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        String fileName = file.getOriginalFilename();
        try {
            // Gọi service để upload file lên MinIO
            return  minioService.uploadFile(file);
        } catch (Exception e) {
            return "Failed to upload file " + fileName + ": " + e.getMessage();
        }
    }
}
