package com.spring.auth.auth_app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@Table(name = "refresh_tokens",indexes = {
   @Index(name = "refresh_token_jti_index",columnList = "jti",unique = true),
        @Index(name = "refresh_token_jti_user_id",columnList = "user_id",unique = true)
})
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name ="jti",updatable = false,nullable = false,unique = true)
    private String jti;
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(nullable = false,updatable = false,name = "user_id")
    private Users user;
      @Column(name = "created_At",nullable = false,updatable = false)
      private Instant createdAt;
    @Column(name ="expires_Att",nullable = false,updatable = false)
    private Instant expiresAt;
    @Column(nullable = false)
     private boolean revoked;
    private String replacedBy;
}
