package com.spring.auth.auth_app.repo;


import com.spring.auth.auth_app.model.Users;
import org.hibernate.sql.ast.tree.expression.JdbcParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<Users,UUID> {
    Optional<Users> findByEmail(String email);
    boolean existsById(UUID id);
}
