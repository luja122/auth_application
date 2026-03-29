package com.spring.auth.auth_app.service;

import com.spring.auth.auth_app.Exception.UserAlreadyExistException;
import com.spring.auth.auth_app.Exception.UserIsNotEnable;
import com.spring.auth.auth_app.dtos.LoginRequest;
import com.spring.auth.auth_app.dtos.LoginResponse;
import com.spring.auth.auth_app.dtos.UsersDto;
import com.spring.auth.auth_app.model.RefreshToken;
import com.spring.auth.auth_app.model.Users;
import com.spring.auth.auth_app.repo.RefreshTokenRepo;
import com.spring.auth.auth_app.repo.UserRepo;
import com.spring.auth.auth_app.security.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    @Autowired
    private final UserRepo userRepo;
    @Autowired
    public final AuthenticationManager authenticationManager;
    @Autowired
    public final JwtService jwtService;
    @Autowired
    public final ModelMapper modelMapper;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final RefreshTokenRepo refreshTokenRepo;
    @Autowired
    private final CookieService cookieService;
    @Override
    public String register(UsersDto data) {
        if(userRepo.existsByEmail(data.getEmail())){
            throw new UserAlreadyExistException("User with this email exists");
        }
        Users user = new Users();
        user.setName(data.getName());
        user.setEmail(data.getEmail());
        user.setPassword(passwordEncoder.encode(data.getPassword()));
        userRepo.save(user);
        return "Sucessfully Registered";
    }

    @Override
    public LoginResponse login (HttpServletResponse response, LoginRequest request) {
        Authentication auth = new UsernamePasswordAuthenticationToken(request.email(),request.password());
        authenticationManager.authenticate(auth); //->If user cannot authenticate the user this throws and error
          Users user =userRepo.findByEmail(request.email()).orElseThrow(()->new UsernameNotFoundException("User not found"));
          if(!user.isEnabled()){
              throw new UserIsNotEnable("User is not available");
          }
          //generate an Refresh token

          var refresh_tokenobj= RefreshToken.builder().jti(UUID.randomUUID().toString()).createdAt(Instant.now()).expiresAt(Instant.now().plusMillis(jwtService.getRefresh())).user(user).revoked(false).build();

          refreshTokenRepo.save(refresh_tokenobj);
           String refresh_Token =jwtService.generateRefreshToken(user,refresh_tokenobj.getJti());
          //generate an acess token
          String acessToken = jwtService.generateAccessToken(user);
          cookieService.attachRefreshTokenCookie(response,acessToken,(int)jwtService.getRefresh());
        return new LoginResponse(acessToken,refresh_Token,jwtService.getAccess(),"",modelMapper.map(user,UsersDto.class)) ;
    }
}
