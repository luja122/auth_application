package com.spring.auth.auth_app.dtos;

import com.spring.auth.auth_app.model.Provider;
import com.spring.auth.auth_app.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsersDto {

    private UUID id;
    private String name;
    private String password;
    private String email;
    private Boolean status=true;
    private byte[] Image_Data;
    private String Image_Name;
    private String Image_Type;
    private Instant createdAt =Instant.now();
    private Instant updatedAt =Instant.now();
    private Set<RoleDto> role = new HashSet<>();
    private Provider provider= Provider.LOCAL;
}
