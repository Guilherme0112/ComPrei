package com.example.loja.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "usuario_enderecos")
public class UsuarioAddress {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "A cidade é obrigatório")
    @Size(min = 2, max = 100, message = "O nome da cidade deve ter entre 2 e 100 caracteres")
    private String cidade;
    
    @NotNull(message = "O bairro e é obrigatório")
    @Size(min = 2, max = 100, message = "O nome do bairro deve ter entre 2 e 100 caracteres")
    private String bairro;
    
    @NotNull(message = "A rua é obrigatório")
    @Size(min = 2, max = 100, message = "O nome da rua deve ter entre 2 e 100 caracteres")
    private String rua;
    
    @NotNull(message = "O CEP é obrigatório")
    @Size(max = 8, min = 8, message = "O CEP deve ter 8 números")
    private String cep;

    private String user_email;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    
}
