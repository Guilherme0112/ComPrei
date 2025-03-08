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
@Table(name = "email_requests")
public class EmailRequests {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String email;

    @Column(updatable = false)
    private LocalDateTime quando = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));


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

    public LocalDateTime getQuando() {
        return quando;
    }

    public void setQuando(LocalDateTime quando) {
        this.quando = quando;
    }

    
}
