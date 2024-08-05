package com.example.imageservice.service.impl;

import com.example.imageservice.entity.Image;
import com.example.imageservice.entity.ImageCard;
import com.example.imageservice.repository.BaseRepository;
import com.example.imageservice.repository.ImageCardRepository;
import com.example.imageservice.service.ImageCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageCardServiceImpl extends BaseServiceImpl<ImageCard> implements ImageCardService {
    @Autowired
    ImageCardRepository imageCardRepository;

    @Override
    protected BaseRepository<ImageCard> getRepository() {
        return imageCardRepository;
    }

    @Override
    public void delete(Long id) {
        ImageCard t = this.getRepository().findAllById(id);
        this.getRepository().delete(t);
    }
}
