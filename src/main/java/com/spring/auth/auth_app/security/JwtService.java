package com.spring.auth.auth_app.security;

import com.spring.auth.auth_app.model.Users;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@Service
public class JwtService {
    @Value("${security.jwt.secret}")
    private final String secretkey;
    @Value("${security.jwt.issuer}")
    private final String issuer;
    @Value("${security.jwt.access_ttl}")
    private final long access;
    @Value("${security.jwt.refresh_ttl}")
    private final long refresh;
    private final SecretKey key = Keys.hmacShaKeyFor(secretkey.getBytes(StandardCharsets.UTF_8));

    public String generateAccessToken(Users user) {

        Instant now = Instant.now();
        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setIssuer(issuer)
                .setSubject(user.getId().toString())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(access / 1000)))
                .addClaims(
                        Map.of(
                                "email", user.getEmail(),
                                "roles", user.getRole(),
                                "typ", "access"
                        ))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateRefreshToken(Users user, String jti) {

        Instant now = Instant.now();
        return Jwts.builder()
                .setId(jti)
                .setSubject(user.getId().toString())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(refresh / 1000)))
                .claim("typ", "refresh")
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public Jws<Claims> parse(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        } catch (JwtException e) {
            throw e;
        }


    }

    public boolean isAcessToken(String token) {
        Claims c = parse(token).getBody();
        return "access".equals(c.get("typ"));
    }
    public boolean isRefreshToken(String token) {
        Claims c = parse(token).getBody();
        return "refresh".equals(c.get("typ"));
    }
    public UUID getUserId(String token){
        Claims c = parse(token).getBody();
        return UUID.fromString(c.getSubject());
    }
    public String getJti(String token){
        return parse(token).getBody().getId();
    }
    public String getEmail(String token){
        Claims c = parse(token).getBody();
        return(String) c.get("email");
    }
    public List<String> getRole(String token){
        Claims c = parse(token).getBody();
        return(List<String>)c.get("role");

    }
}


