package com.example.loja.models;

import java.time.LocalDateTime;
import java.time.ZoneId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "reset_password")
public class ResetPassword {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    private String email;

    @Column(updatable = false)
    private LocalDateTime expire_in = LocalDateTime.now(ZoneId.of("America/Sao_Paulo")).plusMinutes(3);


    public ResetPassword(){ }

    
    public ResetPassword(String token, String email){
        this.email = email;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getExpire_in() {
        return expire_in;
    }

    public void setExpire_in(LocalDateTime expire_in) {
        this.expire_in = expire_in;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    
}
