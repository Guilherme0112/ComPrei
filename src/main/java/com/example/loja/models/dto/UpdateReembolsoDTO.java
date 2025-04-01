package com.example.loja.models.dto;

import java.time.LocalDateTime;

public class UpdateReembolsoDTO {
    
    private Long id;
    private Long idPedido;
    private String nome;
    private String telefone;
    private LocalDateTime quando;

    public UpdateReembolsoDTO() { }

    public UpdateReembolsoDTO(Long id, Long idPedido, String nome, String telefone, LocalDateTime quando) {
        this.id = id;
        this.idPedido = idPedido;
        this.nome = nome;
        this.telefone = telefone;
        this.quando = quando;
     }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getIdPedido() {
        return idPedido;
    }
    public void setIdPedido(Long idPedido) {
        this.idPedido = idPedido;
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
    public LocalDateTime getQuando() {
        return quando;
    }
    public void setQuando(LocalDateTime quando) {
        this.quando = quando;
    }

    
}
