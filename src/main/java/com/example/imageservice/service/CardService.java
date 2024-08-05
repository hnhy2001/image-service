package com.example.imageservice.service;

import com.example.imageservice.entity.Card;
import org.springframework.web.multipart.MultipartFile;

public interface CardService extends BaseService<Card> {
    public String upload(MultipartFile file, Long id);

}
