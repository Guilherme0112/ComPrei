package com.example.loja.models;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "email_verification")
public class VerificationEmail {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String email;

    private String token;
    
    private LocalDateTime expire_in = LocalDateTime.now().plusMinutes(5);


    public VerificationEmail(String email, String token){
        this.token = token;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpire_in() {
        return expire_in;
    }

    public void setExpire_in(LocalDateTime expire_in) {
        this.expire_in = expire_in;
    }


}
