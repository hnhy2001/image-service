package com.example.imageservice.controller;

import com.example.imageservice.service.MinIOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("upload")
@CrossOrigin
public class UploadController {
    @Autowired
    private MinIOService minioService;

    @PostMapping()
    public String handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam Long customerId) {
        String fileName = file.getOriginalFilename();
        try {
            // Gọi service để upload file lên MinIO
            return  minioService.uploadFile(file, customerId);
        } catch (Exception e) {
            return "Failed to upload file " + fileName + ": " + e.getMessage();
        }
    }

//    @GetMapping("/{bucket}/{name}")
//    public ResponseEntity<InputStreamResource> getNameAndBucket(@PathVariable String bucket, @PathVariable String name) {
//        try {
//            InputStream inputStream = minioService.getFileWithBucketName(bucket, name);
//
//            return ResponseEntity.ok()
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + name)
//                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                    .body(new InputStreamResource(inputStream));
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().body(null);
//        }
//    }

    @GetMapping("/{bucket}")
    public List<String> getFileNameWithBucket(@PathVariable String bucket) {
        return minioService.getAllFileNameWithBucketName(bucket);
    }

    @GetMapping("/{bucket}/{object}")
    public String getFileNameWithBucket(@PathVariable String bucket, @PathVariable String object) {
        return minioService.getPresignedObjectUrl(bucket, object);
    }

    @GetMapping("/get-all-bucket")
    public List<String> getAllBucketNames() {
        return minioService.getAllBucket();
    }

}
