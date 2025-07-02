package com.example.chat_app.backend;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository
        extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmail(String email);
}