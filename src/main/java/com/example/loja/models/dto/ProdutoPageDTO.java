package com.example.loja.models.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.loja.models.Produto;

public class ProdutoPageDTO {

    private List<Produto> produtos;
    private int paginaAtual;
    private int totalPaginas;
    private long totalElementos;

    public ProdutoPageDTO(Page<Produto> page) {
        this.produtos = page.getContent();
        this.paginaAtual = page.getNumber();
        this.totalPaginas = page.getTotalPages();
        this.totalElementos = page.getTotalElements();
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public int getPaginaAtual() {
        return paginaAtual;
    }

    public int getTotalPaginas() {
        return totalPaginas;
    }

    public long getTotalElementos() {
        return totalElementos;
    }
}
