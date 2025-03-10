package com.example.loja.service.AdminService;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.loja.models.Produto;
import com.example.loja.repositories.ProdutoRepository;

@Service
public class APIAdminProdutosService {
    
    private final ProdutoRepository produtoRepository;

    public APIAdminProdutosService(ProdutoRepository produtoRepository){
        this.produtoRepository = produtoRepository;
    }

    public List<Produto> getOffsetData(String offset){


        return produtoRepository.findAll();

    }
}
