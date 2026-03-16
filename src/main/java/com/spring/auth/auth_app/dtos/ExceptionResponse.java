package com.spring.auth.auth_app.dtos;

import org.springframework.http.HttpStatus;

public record ExceptionResponse(
        String message,
        HttpStatus status,
        int statuscode
){


}
