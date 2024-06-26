package com.example.imageservice.service;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
public class MinIOService {

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @Value("${minio.url}")
    private String minioUrl;

    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;

    public String uploadFile(MultipartFile file) {
        try {
            // Kiểm tra nếu bucket không tồn tại thì tạo mới
            boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!isExist) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

            // Upload file lên MinIO
            minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(file.getOriginalFilename()).stream(file.getInputStream(),file.getInputStream().available(), -1 ).contentType("image/jpeg").build());


            return "File uploaded successfully: " + file.getOriginalFilename();
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            System.err.println("Error occurred: " + e);
            return e.getMessage();
        }
    }
}
