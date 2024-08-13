package com.example.imageservice.service.impl;

import com.example.imageservice.entity.Customer;
import com.example.imageservice.entity.Image;
import com.example.imageservice.entity.User;
import com.example.imageservice.service.CustomerService;
import com.example.imageservice.service.ImageService;
import com.example.imageservice.service.MinIOService;
import com.example.imageservice.service.UserService;
import com.example.imageservice.util.DateUtil;
import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class MinIOServiceImpl implements MinIOService {
    @Autowired
    private MinioClient minioClient;

    @Autowired
    private ImageService imageService;

    @Autowired
    private CustomerService customerService;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @Override
    public String uploadFile(MultipartFile file, Long id) {
        try {
            Customer customer = customerService.getById(id);
            // Kiểm tra nếu bucket không tồn tại thì tạo mới
            boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!isExist) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

            String forder = customer.getUser().getFullName().replaceAll("", "") + "/" + customer.getName().replaceAll("", "") + "_" + DateUtil.getCurrenDateTime() + "/" + file.getOriginalFilename();
            // Upload file lên MinIO
            minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(forder).stream(file.getInputStream(), file.getInputStream().available(), -1).contentType("image/jpeg").build());
//            String presignedObjectUrl = minioClient.getPresignedObjectUrl(
//                    GetPresignedObjectUrlArgs.builder().method(Method.GET).bucket(bucketName).object(forder).build()
//            );
            Image image = Image.builder().url(forder).customer(customer).build();
            imageService.create(image);
            return "File uploaded successfully: " + file.getOriginalFilename();
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            System.err.println("Error occurred: " + e);
            return e.getMessage();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
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

    @Override
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

    @Override
    public String getPresignedObjectUrl(String bucketName, String objectName) {
        try {
            // Generate presigned URL for an object with 1 hour expiration
            String presignedObjectUrl = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder().method(Method.GET).bucket(bucketName).object(objectName).build()
            );
            return presignedObjectUrl;
        } catch (MinioException e) {
            throw new RuntimeException("Error occurred: " + e);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred: " + e);
        }
    }

    @Override
    public List<Image> getAllFileWithName(List<Image> req) {
        req.stream().forEach(e -> {
            try {
                String presignedObjectUrl = minioClient.getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs.builder().method(Method.GET).bucket(bucketName).object(e.getUrl()).build()
                );
                e.setUrl(presignedObjectUrl);
            } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException ex) {
                System.err.println("Error occurred: " + ex);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        return req;
    }
}
