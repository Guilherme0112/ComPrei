package com.example.loja.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;

import com.example.loja.enums.Pedido;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "pedidos")
public class Pedidos {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String produtos_codigo;

    private String payment_id;

    private String email;

    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Pedido status;

    @Column(updatable = false)
    private LocalDateTime quando = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));

    public Pedidos(){ }

    public Pedidos(String payment_id, BigDecimal valor, String produtos_codigo, String email, Pedido status){ 
        this.produtos_codigo = produtos_codigo;
        this.payment_id = payment_id;
        this.email = email;
        this.status = status;
        this.valor = valor;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Pedido getStatus() {
        return status;
    }

    public void setStatus(Pedido status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProdutos_codigo() {
        return produtos_codigo;
    }

    public void setProdutos_codigo(String produtos_codigo) {
        this.produtos_codigo = produtos_codigo;
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
