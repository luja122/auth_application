package com.spring.auth.auth_app.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateDto {
    private String name;
    private String password;
    private byte[] Image_Data;
    private String Image_Name;
    private String Image_Type;
}
