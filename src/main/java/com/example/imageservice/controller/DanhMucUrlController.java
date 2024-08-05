package com.example.imageservice.controller;

import com.example.imageservice.entity.DanhMucUrl;
import com.example.imageservice.service.BaseService;
import com.example.imageservice.service.DanhMucUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("url")
public class DanhMucUrlController extends BaseController<DanhMucUrl> {
    @Autowired
    private DanhMucUrlService danhMucUrlService;

    @Override
    protected BaseService<DanhMucUrl> getService() {
        return danhMucUrlService;
    }
}
