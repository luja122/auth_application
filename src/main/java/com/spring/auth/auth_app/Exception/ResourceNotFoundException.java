package com.spring.auth.auth_app.Exception;

public class ResourceNotFoundException extends RuntimeException{
public ResourceNotFoundException(String message){
    super(message);
}
public ResourceNotFoundException(){
    System.out.println("Resource Not Found");
}
}
