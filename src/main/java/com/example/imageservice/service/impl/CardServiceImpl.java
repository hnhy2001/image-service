package com.example.imageservice.service.impl;

import com.example.imageservice.entity.Card;
import com.example.imageservice.entity.Customer;
import com.example.imageservice.entity.Image;
import com.example.imageservice.entity.ImageCard;
import com.example.imageservice.repository.BaseRepository;
import com.example.imageservice.repository.CardRepository;
import com.example.imageservice.service.CardService;
import com.example.imageservice.service.CustomerService;
import com.example.imageservice.service.ImageCardService;
import com.example.imageservice.service.ImageService;
import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class CardServiceImpl extends BaseServiceImpl<Card> implements CardService {
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    ImageCardService imageCardService;

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @Override
    protected BaseRepository<Card> getRepository() {
        return cardRepository;
    }

    @Override
    public String upload(MultipartFile file, Long id) {
        try {
            Card card = super.getById(id);
            // Kiểm tra nếu bucket không tồn tại thì tạo mới
            boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!isExist) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

            String forder = card.getCustomer().getName().replaceAll("","") + "/" + card.getBank().replaceAll("","") + "/" + file.getOriginalFilename();
            // Upload file lên MinIO
            minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(forder).stream(file.getInputStream(), file.getInputStream().available(), -1).contentType("image/jpeg").build());
            String presignedObjectUrl = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder().method(Method.GET).bucket(bucketName).object(forder).build()
            );
            ImageCard image = ImageCard.builder().url(presignedObjectUrl).card(card).build();
            imageCardService.create(image);
            return "File uploaded successfully: " + file.getOriginalFilename();
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            System.err.println("Error occurred: " + e);
            return e.getMessage();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
