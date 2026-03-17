package com.spring.auth.auth_app.service;

import com.spring.auth.auth_app.Exception.UserAlreadyExistException;
import com.spring.auth.auth_app.dtos.UsersDto;
import com.spring.auth.auth_app.model.Users;
import com.spring.auth.auth_app.repo.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    @Autowired
    private final UserRepo userRepo;
    @Override
    public String register(UsersDto data) {
        if(userRepo.existsByEmail(data.getEmail())){
            throw new UserAlreadyExistException("User with this email exists");
        }
        Users user = new Users();
        user.setName(data.getName());
        user.setEmail(data.getEmail());
        user.setPassword(data.getPassword());
        userRepo.save(user);
        return "Sucessfully Registered";
    }
}
