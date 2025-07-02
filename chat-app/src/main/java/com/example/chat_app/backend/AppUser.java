package com.example.chat_app.backend;

import jakarta.persistence.*;

@Entity
@Table(name="users")
public class AppUser {
  @Id @GeneratedValue private Long id;
  @Column(unique=true) private String email;
  private String passwordHash;

  // getters
  public Long   getId()           { return id; }
  public String getEmail()        { return email; }
  public String getPasswordHash() { return passwordHash; }

  // setters
  public void setEmail(String e)        { this.email = e; }
  public void setPasswordHash(String h) { this.passwordHash = h; }
}