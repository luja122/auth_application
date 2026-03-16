package com.spring.auth.auth_app.service;

import com.spring.auth.auth_app.dtos.UserUpdateDto;
import com.spring.auth.auth_app.dtos.UsersDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
   public List<UsersDto> getAllUsers();
   public UsersDto getUserById(String id);
   public UsersDto updateUsers(UserUpdateDto userUpdateDto, MultipartFile image, String id);
   public void deleteUser(String id);
}
