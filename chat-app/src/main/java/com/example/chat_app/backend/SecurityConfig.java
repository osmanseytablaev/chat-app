package com.example.chat_app.backend;

import com.example.chat_app.backend.AppUser;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.User;              
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /** BCrypt w/ default strength 10 */
    @Bean
    PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    /** tell Spring how to fetch user details */
    @Bean
    UserDetailsService uds(UserService userService) {
        return username -> {
            AppUser u = userService.load(username);
            return User                                   // Spring's builder
                     .withUsername(u.getEmail())
                     .password(u.getPasswordHash())
                     .roles("USER")                       // single role
                     .build();
        };
    }

    /** main filter chain => session-based login/logout */
    @Bean
    SecurityFilterChain chain(HttpSecurity http) throws Exception {

        http.csrf().disable();                          // KEEP OFF only in dev

        http.authorizeHttpRequests(auth -> auth
               .requestMatchers(
                     "/api/auth/register",
                     "/api/auth/login",
                     "/", "/index.html",
                     "/css/**", "/js/**", "/images/**",
                     "/h2-console/**"                   // dev db console
               ).permitAll()
               .anyRequest().authenticated());

        // built-in form login; we set custom URL
        http.formLogin(login -> login
               .loginProcessingUrl("/api/auth/login")
               .successHandler((req,res,auth) -> res.setStatus(200))
               .failureHandler((req,res,ex) ->  res.setStatus(401)));

        http.logout(logout -> logout
               .logoutUrl("/api/auth/logout")
               .logoutSuccessHandler((req,res,auth) -> res.setStatus(204)));

        // allow H2 frame in dev
        http.headers().frameOptions().disable();

        return http.build();
    }
}
