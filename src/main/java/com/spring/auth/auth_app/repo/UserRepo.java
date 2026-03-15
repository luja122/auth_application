package com.spring.auth.auth_app.repo;


import java.util.Optional;
import java.util.UUID;

public interface UserRepo {
    Optional findByName(String name);
    boolean existsById(UUID id);
}
