package com.spring.auth.auth_app.Exception;

public class UserAlreadyExistException extends RuntimeException{
    public UserAlreadyExistException(String message){
        super(message);
    }
    public UserAlreadyExistException(){
        System.out.println("User with that email already exist");
    }
}
