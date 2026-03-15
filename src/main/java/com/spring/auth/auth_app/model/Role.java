package com.spring.auth.auth_app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="role")
public class Role {
    @Id
    @Column(name ="role_id")
    private UUID id = UUID.randomUUID();
    @Column(unique = true,nullable = false)
    private String role;
}
