package com.spring.auth.auth_app.controller;

import com.spring.auth.auth_app.dtos.LoginRequest;
import com.spring.auth.auth_app.dtos.LoginResponse;
import com.spring.auth.auth_app.dtos.UsersDto;
import com.spring.auth.auth_app.service.AuthService;
import com.spring.auth.auth_app.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    @Autowired
    private final AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request){
      LoginResponse response = authService.login(request);
      return ResponseEntity.ok(response);
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UsersDto data){
        return ResponseEntity.ok().body(authService.register(data));
    }


}
