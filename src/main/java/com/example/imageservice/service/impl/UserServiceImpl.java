package com.example.imageservice.service.impl;

import com.example.imageservice.constants.Status;
import com.example.imageservice.entity.User;
import com.example.imageservice.model.request.ChangePasswordReq;
import com.example.imageservice.model.request.LoginRequest;
import com.example.imageservice.model.request.SearchReq;
import com.example.imageservice.model.response.BaseResponse;
import com.example.imageservice.model.response.LoginResponse;
import com.example.imageservice.query.CustomRsqlVisitor;
import com.example.imageservice.repository.BaseRepository;
import com.example.imageservice.repository.UserRepository;
import com.example.imageservice.service.UserService;
import com.example.imageservice.util.ContextUtil;
import com.example.imageservice.util.DateUtil;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.example.imageservice.config.jwt.JwtTokenProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private ContextUtil contextUtil;

    @Override
    protected BaseRepository<User> getRepository() {
        return userRepository;
    }

    @Override
    public BaseResponse login(LoginRequest loginRequest) throws Exception {
        Optional<User> userOptional = userRepository.findByUserName(loginRequest.getUsername());
        if (!userOptional.isPresent())
            return new BaseResponse(500, "Account không tồn tại", null);

        User user = userOptional.get();
        if (!Objects.equals(user.getIsActive(), Status.ACTIVE))
            return new BaseResponse(500, "Account đã bị khóa", null);

        if (!isValidPassword(user.getPassword(), loginRequest.getPassword())) {
            return new BaseResponse(500, "Mật khẩu không chính xác", null);
        }
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtTokenProvider.generateToken(user.getUserName()));
        loginResponse.setUserName(user.getUserName());
        loginResponse.setFullName(user.getFullName());
        loginResponse.setRole(user.getRole());
        loginResponse.setUserId(user.getId());
        return new BaseResponse(200, "OK", loginResponse);
    }

    @Override
    public BaseResponse customCreate(User user) throws Exception {
        if (user.getUserName() == null){
            return new BaseResponse().fail("Tài khoản không được để trống");
        }
        if (user.getPassword() == null){
            return new BaseResponse().fail("Mật khẩu không được để trống");
        }

        if (userRepository.findByUserName(user.getUserName()).isPresent()){
            return new BaseResponse().fail("Tài khoản đã tồn tại");
        }
        user.setCreateDate(DateUtil.getCurrenDateTime());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setIsActive(1);
        User result = super.create(user);
        return new BaseResponse().success(result);
    }

    @Override
    public BaseResponse changePassword(ChangePasswordReq changePasswordReq) {
        User user = userRepository.findAllById(changePasswordReq.getUserId());
        if (user == null){
            return new BaseResponse().fail("Tài khoản không tồn tại");
        }
        user.setPassword(passwordEncoder.encode(changePasswordReq.getNewPassword()));
        userRepository.save(user);
        return new BaseResponse().success("Thay đổi mật khẩu thành công");
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUserName(username).get();
    }


    private boolean isValidPassword(String userPass, String reqPass) {
        return !StringUtils.isEmpty(reqPass) && passwordEncoder.matches(reqPass, userPass);
    }
}
