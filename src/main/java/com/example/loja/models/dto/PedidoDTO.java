package com.example.loja.models.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PedidoDTO {

    private Long id;
    private String nome;
    private String telefone;
    private BigDecimal valor;
    private String status;
    private LocalDateTime quando;
    private String descricao;

    public PedidoDTO() {
    }

    public PedidoDTO(Long id, String nome, String telefone, BigDecimal valor, String status, LocalDateTime quando) {

        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.valor = valor;
        this.status = status;
        this.quando = quando;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getQuando() {
        return quando;
    }

    public void setQuando(LocalDateTime quando) {
        this.quando = quando;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
