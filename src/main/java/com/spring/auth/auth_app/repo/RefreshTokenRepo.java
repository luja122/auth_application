package com.spring.auth.auth_app.repo;

import com.spring.auth.auth_app.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface RefreshTokenRepo extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByJti(String jti);
    void deleteById(UUID id);
}
