package com.example.imageservice.repository;

import com.example.imageservice.entity.User;

import java.util.Optional;

public interface UserRepository extends BaseRepository<User>{
    Optional<User> findByUserName(String userName);

}
