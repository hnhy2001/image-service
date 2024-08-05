package com.example.imageservice.service.impl;

import com.example.imageservice.constants.Status;
import com.example.imageservice.entity.Image;
import com.example.imageservice.repository.BaseRepository;
import com.example.imageservice.repository.ImageRepository;
import com.example.imageservice.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageServiceImpl extends BaseServiceImpl<Image> implements ImageService {
    @Autowired
    private ImageRepository imageRepository;

    @Override
    protected BaseRepository<Image> getRepository() {
        return imageRepository;
    }

    @Override
    public void delete(Long id) {
        Image t = this.getRepository().findAllById(id);
        this.getRepository().delete(t);
    }
}
