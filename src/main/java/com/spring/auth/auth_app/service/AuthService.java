package com.spring.auth.auth_app.service;

import com.spring.auth.auth_app.dtos.UsersDto;

public interface AuthService {
    public String register(UsersDto data);
}
