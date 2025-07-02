package com.example.chat_app.backend;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService service;

    public AuthController(UserService service) { this.service = service; }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Validated Credentials c) {
        service.register(c.email(), c.password());
        return ResponseEntity.status(201).build();
    }
}

