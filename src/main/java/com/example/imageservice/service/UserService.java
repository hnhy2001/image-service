package com.example.imageservice.service;

import com.example.imageservice.entity.User;
import com.example.imageservice.model.request.ChangePasswordReq;
import com.example.imageservice.model.request.LoginRequest;
import com.example.imageservice.model.response.BaseResponse;

public interface UserService extends BaseService<User> {
    BaseResponse login(LoginRequest loginRequest) throws Exception;
    BaseResponse customCreate(User user) throws Exception;
    BaseResponse changePassword(ChangePasswordReq changePasswordReq);
    User getUserByUsername(String username);
}
