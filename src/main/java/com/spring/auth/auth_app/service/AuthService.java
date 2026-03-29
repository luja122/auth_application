package com.spring.auth.auth_app.service;

import com.spring.auth.auth_app.dtos.LoginRequest;
import com.spring.auth.auth_app.dtos.LoginResponse;
import com.spring.auth.auth_app.dtos.UsersDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    public String register(UsersDto data);

    public LoginResponse login(HttpServletResponse reponse,LoginRequest request);
}
