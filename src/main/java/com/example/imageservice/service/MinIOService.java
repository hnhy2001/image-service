package com.example.imageservice.service;

import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
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
import java.util.ArrayList;
import java.util.List;

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
            minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(file.getOriginalFilename()).stream(file.getInputStream(), file.getInputStream().available(), -1).contentType("image/jpeg").build());


            return "File uploaded successfully: " + file.getOriginalFilename();
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            System.err.println("Error occurred: " + e);
            return e.getMessage();
        }
    }

    public List<String> getAllFileNameWithBucketName(String bucketName) {
        List<String> objectNames = new ArrayList<>();
        try {
            boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!isExist) {
                throw new RuntimeException("Bucket does not exist: " + bucketName);
            }

            Iterable<Result<Item>> objects = minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).build());
            for (Result<Item> result : objects) {
                Item item = result.get();
                objectNames.add(item.objectName());
            }
            return objectNames;
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            System.err.println("Error occurred: " + e);
            return null;
        }
    }

    public List<String> getAllBucket() {
        List<String> bucketNames = new ArrayList<>();
        try {
            List<Bucket> buckets = minioClient.listBuckets();
            for (Bucket bucket : buckets) {
                bucketNames.add(bucket.name());
            }
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            System.err.println("Error occurred: " + e);
            return null;
        }
        return bucketNames;
    }
}
