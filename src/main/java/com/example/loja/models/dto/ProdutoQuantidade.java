package com.example.loja.models.dto;

public class ProdutoQuantidade {

    private String codigo;
    private Integer quantidade;
    
    public ProdutoQuantidade(){ }

    public String getCodigo() {
        return codigo;
    }
    public void setProduto(String codigo) {
        this.codigo = codigo;
    }
    public Integer getQuantidade() {
        return quantidade;
    }
    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    
}
