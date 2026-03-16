package com.spring.auth.auth_app.helper;

import java.util.UUID;

public class UserHelper {
    public static UUID praseUuid(String id){
        return UUID.fromString(id);
    }
}
