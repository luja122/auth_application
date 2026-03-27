package com.spring.auth.auth_app.Exception;

public class UserIsNotEnable extends RuntimeException {
    public UserIsNotEnable(String message) {
        super(message);
    }
    public UserIsNotEnable(){
        System.out.println("User is not Enabled yet!!");
    }
}
