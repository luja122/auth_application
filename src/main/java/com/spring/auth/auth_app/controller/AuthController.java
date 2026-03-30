package com.spring.auth.auth_app.controller;

import com.spring.auth.auth_app.dtos.LoginRequest;
import com.spring.auth.auth_app.dtos.LoginResponse;
import com.spring.auth.auth_app.dtos.RefreshTokenRequest;
import com.spring.auth.auth_app.dtos.UsersDto;
import com.spring.auth.auth_app.model.RefreshToken;
import com.spring.auth.auth_app.model.Users;
import com.spring.auth.auth_app.repo.RefreshTokenRepo;
import com.spring.auth.auth_app.security.JwtService;
import com.spring.auth.auth_app.service.AuthService;
import com.spring.auth.auth_app.service.CookieService;
import com.spring.auth.auth_app.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    @Autowired
    private final AuthService authService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private RefreshTokenRepo refreshTokenRepo;
    @Autowired
    private CookieService cookieService;
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(HttpServletResponse reponse, @RequestBody LoginRequest request){
      LoginResponse response = authService.login(reponse,request);
      return ResponseEntity.ok(response);
    }
    public ResponseEntity<LoginResponse> refreshToken(
            @RequestBody(required = false) RefreshTokenRequest body,
            HttpServletResponse response,
            HttpServletRequest request
    ){
        //read refresh tokenfrom the body or request;
        String refreshToken = authService.readRefreshToken(body,request).orElseThrow(()->new BadCredentialsException("Refresh Token not found"));
        UUID userId = jwtService.getUserId(refreshToken);
        String jti = jwtService.getJti(refreshToken);

        RefreshToken token = refreshTokenRepo.findByJti(jti).orElseThrow(()->new BadCredentialsException("not found"));
         if(!jwtService.isRefreshToken(refreshToken)){
        throw new BadCredentialsException("Invalid refresh token");
         }
         if(token.isRevoked()){
             throw new BadCredentialsException("IS Revoked");
         }
           if(token.getExpiresAt().isBefore(Instant.now())){
           throw new BadCredentialsException("IS expired");
              }
           token.setRevoked(true);
           String newjti = UUID.randomUUID().toString();
        token.setReplacedBy(newjti);
        refreshTokenRepo.save(token);
        Users user = token.getUser();
        RefreshToken newRefreshToken  = RefreshToken.builder().jti(newjti).user(user).createdAt(Instant.now()).expiresAt(Instant.now().minusMillis(jwtService.getRefresh())).revoked(false).build();
          refreshTokenRepo.save(newRefreshToken);
          String newacessToken = jwtService.generateAccessToken(user);
          String newrefreshToken = jwtService.generateRefreshToken(user,newjti);
          cookieService.attachRefreshTokenCookie(response,newrefreshToken,(int) jwtService.getRefresh());
          cookieService.addNoHeader(response);
          return ResponseEntity.ok(new LoginResponse (newacessToken,newrefreshToken,jwtService.getRefresh(),"",modelMapper.map(user,UsersDto.class)));

    }
    @DeleteMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {

        // 1️⃣ Get refresh token from cookie
        Optional<String> refreshTokenOpt = Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals("refreshToken"))
                .map(Cookie::getValue)
                .findFirst();

        if(refreshTokenOpt.isPresent()) {
            String refreshToken = refreshTokenOpt.get();
                UUID id = jwtService.getUserId(refreshToken);
            // 2️⃣ Delete refresh token from DB
            refreshTokenRepo.deleteById(id);
        }

        return ResponseEntity.ok("Logged out successfully");
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UsersDto data){
        return ResponseEntity.ok().body(authService.register(data));
    }



}
