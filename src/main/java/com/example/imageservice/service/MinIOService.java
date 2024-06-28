package com.example.imageservice.service;

import com.example.imageservice.entity.Customer;
import com.example.imageservice.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MinIOService {

    public String uploadFile(MultipartFile file, Long id);

    public List<String> getAllFileNameWithBucketName(String bucketName);

    public List<String> getAllBucket();

    public String getPresignedObjectUrl(String bucketName, String objectName);
}
