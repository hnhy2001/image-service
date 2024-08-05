package com.example.imageservice.service.impl;

import com.example.imageservice.entity.DanhMucUrl;
import com.example.imageservice.repository.BaseRepository;
import com.example.imageservice.repository.DanhMucUrlRepository;
import com.example.imageservice.service.DanhMucUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DanhMucUrlServiceImpl extends BaseServiceImpl<DanhMucUrl> implements DanhMucUrlService {
    @Autowired
    private DanhMucUrlRepository danhMucUrlRepository;

    @Override
    protected BaseRepository<DanhMucUrl> getRepository() {
        return danhMucUrlRepository;
    }
}
