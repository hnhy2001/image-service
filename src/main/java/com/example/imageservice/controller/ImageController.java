package com.example.imageservice.controller;

import com.example.imageservice.entity.Image;
import com.example.imageservice.service.BaseService;
import com.example.imageservice.service.ImageService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("image")
public class ImageController extends BaseController<Image>{
    @Autowired
    private ImageService imageService;

    @Override
    protected BaseService<Image> getService() {
        return imageService;
    }
}
