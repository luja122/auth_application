package com.spring.auth.auth_app.dtos;

public record LoginResponse(
        String acessToken,
        String refreshToken,
        long expiredAt,
        String typ,
        UsersDto user
) {

}
