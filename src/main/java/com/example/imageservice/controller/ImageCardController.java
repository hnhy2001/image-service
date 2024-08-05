package com.example.imageservice.controller;

import com.example.imageservice.entity.ImageCard;
import com.example.imageservice.service.BaseService;
import com.example.imageservice.service.ImageCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("image-card")
public class ImageCardController extends BaseController<ImageCard>{
    @Autowired
    private ImageCardService imageCardService;
    @Override
    protected BaseService<ImageCard> getService() {
        return imageCardService;
    }
}
