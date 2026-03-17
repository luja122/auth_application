package com.spring.auth.auth_app.Exception;

import com.spring.auth.auth_app.dtos.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handlleResourceNotFoundException(ResourceNotFoundException exception){
        ExceptionResponse internalServerError = new ExceptionResponse(exception.getMessage(), HttpStatus.NOT_FOUND,4040);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(internalServerError);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handlleIllegalArgumentException(IllegalArgumentException exception){
        ExceptionResponse internalServerError = new ExceptionResponse(exception.getMessage(), HttpStatus.BAD_REQUEST,400);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(internalServerError);
    }
    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ExceptionResponse> handlerUserAlreadyExistException(UserAlreadyExistException exception){
       ExceptionResponse internalServerError= new ExceptionResponse(exception.getMessage(),HttpStatus.NOT_FOUND,404);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(internalServerError);
    }
}
