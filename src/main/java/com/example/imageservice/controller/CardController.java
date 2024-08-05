package com.example.imageservice.controller;

import com.example.imageservice.entity.Card;
import com.example.imageservice.service.BaseService;
import com.example.imageservice.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
@RequestMapping("card")
public class CardController extends BaseController<Card>{
    @Autowired
    private CardService cardService;

    @Override
    protected BaseService<Card> getService() {
        return cardService;
    }

    @PostMapping("upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam Long cardId) {
        String fileName = file.getOriginalFilename();
        try {
            // Gọi service để upload file lên MinIO
            return  cardService.upload(file, cardId);
        } catch (Exception e) {
            return "Failed to upload file " + fileName + ": " + e.getMessage();
        }
    }
}
