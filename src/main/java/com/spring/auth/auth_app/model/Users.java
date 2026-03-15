package com.spring.auth.auth_app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
@Entity
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    private UUID id;
    @Column(name ="user_name",unique = true)
    @NotBlank
    private String name;
    @NotBlank
    private String password;
    @NotBlank
    @Email
    private String email;
    private boolean status=true;
    @Lob
    private byte[] Image_Data;
    private String Image_Name;
    private String Image_Type;
    private Instant createdAt =Instant.now();
    private Instant updatedAt =Instant.now();
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "roles",
            joinColumns =@JoinColumn(name="user_id"), inverseJoinColumns = @JoinColumn(name="role_id"))
    private Set<Role> role = new HashSet<>();
    @Enumerated(EnumType.STRING)
    private Provider provider= Provider.LOCAL;
}
