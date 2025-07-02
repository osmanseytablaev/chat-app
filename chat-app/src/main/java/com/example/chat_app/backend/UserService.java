package com.example.chat_app.backend;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repo,
                       PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    /** throws if email already exists */
    public void register(String email, String rawPw) {
        if (repo.findByEmail(email).isPresent())
            throw new IllegalStateException("email taken");

        AppUser u = new AppUser();
        u.setEmail(email);
        u.setPasswordHash(encoder.encode(rawPw));
        repo.save(u);
    }

    /** used by Spring-Security */
    public AppUser load(String email) {
        return repo.findByEmail(email)
                   .orElseThrow(
                     () -> new UsernameNotFoundException(email));
    }
}
