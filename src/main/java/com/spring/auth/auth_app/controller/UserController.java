package com.spring.auth.auth_app.controller;

import com.spring.auth.auth_app.dtos.UserUpdateDto;
import com.spring.auth.auth_app.dtos.UsersDto;
import com.spring.auth.auth_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/v1/api")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/user")
    public ResponseEntity<List<UsersDto>> getAllUsers(){
        return ResponseEntity.status(HttpStatus.FOUND).body(userService.getAllUsers());
    }
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable String id){
        userService.deleteUser(id);
    }
    @PutMapping("/users/{id}")
    public ResponseEntity<Object> updateUsers(UserUpdateDto userUpdateDto, MultipartFile image, String id){
        return ResponseEntity.ok().body(userService.updateUsers(userUpdateDto,image,id));
    }
    @GetMapping("/user/{id}")
    public ResponseEntity<UsersDto> getAllUsers(@PathVariable String id){
        return ResponseEntity.status(HttpStatus.FOUND).body(userService.getUserById(id));
    }

}
