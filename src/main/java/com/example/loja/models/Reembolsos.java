package com.example.loja.models;

import java.time.LocalDateTime;
import java.time.ZoneId;

import com.example.loja.enums.Reembolso;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "reembolsos")
public class Reembolsos {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long idPedido;

    private String email;

    @Enumerated(EnumType.STRING)
    private Reembolso status;

    @Column(updatable = false)
    private LocalDateTime quando = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));

    public Reembolsos(){ }

    public Reembolsos(String email, Long idPedido, Reembolso status){
        this.email = email;
        this.idPedido = idPedido;
        this.status = status;
    }

    
    public Long getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Long idPedido) {
        this.idPedido = idPedido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPedido() {
        return idPedido;
    }

    public void setPedido(Long idPedido) {
        this.idPedido = idPedido;
    }

    public Reembolso getStatus() {
        return status;
    }

    public void setStatus(Reembolso status) {
        this.status = status;
    }

    public LocalDateTime getQuando() {
        return quando;
    }

    public void setQuando(LocalDateTime quando) {
        this.quando = quando;
    }

    
}
