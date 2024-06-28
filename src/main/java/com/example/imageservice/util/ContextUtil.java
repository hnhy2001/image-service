package com.example.imageservice.util;

import com.example.imageservice.entity.User;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class ContextUtil {
    private String userName;
}
