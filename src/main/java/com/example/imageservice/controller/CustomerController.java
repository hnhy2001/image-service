package com.example.imageservice.controller;

import com.example.imageservice.entity.Customer;
import com.example.imageservice.model.request.SearchReq;
import com.example.imageservice.model.response.BaseResponse;
import com.example.imageservice.service.BaseService;
import com.example.imageservice.service.CustomerService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
@RequestMapping("customer")
public class CustomerController{
    @Autowired
    CustomerService customerService;

    @PostMapping("/create")
    public BaseResponse create(@RequestBody Customer customer) throws Exception {
        return new BaseResponse(200, "Tạo thành công!", customerService.create(customer));
    }

    @GetMapping("/search")
//    @PreAuthorize("@appAuthorizer.authorize(authentication, 'VIEW', this)")
    public BaseResponse search(SearchReq req) {
        return new BaseResponse(200, "Lấy dữ liệu thành công!", customerService.search(req));
    }

    @GetMapping("/detail")
    public BaseResponse getById(@RequestParam(value = "id") Long id) throws Exception {
        return new BaseResponse(200, "Lấy dữ liệu thành công!", customerService.getById(id));
    }

    @PutMapping("/update")
    public BaseResponse update(@RequestBody Customer t) throws Exception {
        return new BaseResponse(200, "Cập nhật thành công!", customerService.update(t));
    }


    @DeleteMapping("/delete")
    public BaseResponse deleteById(@RequestParam(name = "id") Long id) {
        customerService.delete(id);
        return new BaseResponse(200, "Xóa thành công!");
    }

}
