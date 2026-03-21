package com.spring.auth.auth_app.service;

import com.spring.auth.auth_app.Exception.ResourceNotFoundException;
import com.spring.auth.auth_app.security.UserPrinciple;
import com.spring.auth.auth_app.model.Users;
import com.spring.auth.auth_app.repo.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


@AllArgsConstructor
public class MyUserService implements UserDetailsService {
    @Autowired
    public final UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String input) throws UsernameNotFoundException {
        Users user = userRepo.findByEmail(input).orElseThrow(()-> new ResourceNotFoundException("The Gmail does not Exist"));
return new UserPrinciple(user);
    }
}
