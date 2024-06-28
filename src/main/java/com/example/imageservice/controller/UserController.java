package com.example.imageservice.controller;

import com.example.imageservice.entity.User;
import com.example.imageservice.model.request.ChangePasswordReq;
import com.example.imageservice.model.request.LoginRequest;
import com.example.imageservice.model.response.BaseResponse;
import com.example.imageservice.service.BaseService;
import com.example.imageservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("user")
public class UserController extends BaseController<User> {
    @Autowired
    private UserService userService;

    @Override
    protected BaseService<User> getService() {
        return userService;
    }

    @PostMapping("/login")
    public BaseResponse login(@RequestBody LoginRequest loginRequest) throws Exception {
        return userService.login(loginRequest);
    }

    @Override
    @PostMapping("/create")
    public BaseResponse create(@RequestBody User user) throws Exception {
        return userService.customCreate(user);
    }

    @PostMapping("/change-password")
    public BaseResponse changePassword(@RequestBody ChangePasswordReq changePasswordReq) {
        return userService.changePassword(changePasswordReq);
    }
}
