package com.example.loja.controllers.ComprasController;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.loja.exceptions.ProdutoException;
import com.example.loja.models.Produto;
import com.example.loja.models.dto.ProdutoPageDTO;
import com.example.loja.repositories.ProdutoRepository;

@RestController
public class APIProdutosController {
    
    private final ProdutoRepository produtoRepository;

    public APIProdutosController(ProdutoRepository produtoRepository){
        this.produtoRepository = produtoRepository;
    }

    @GetMapping("/produto/get/{i}")
    public ProdutoPageDTO infinitePage(@PathVariable("i") String i) throws Exception, ProdutoException{

        try {

            int indice = Integer.parseInt(i);
            
            Pageable pageable = PageRequest.of(indice, 5);

            Page<Produto> res = produtoRepository.findRandom(pageable);

            return new ProdutoPageDTO(res);

        } catch (Exception e) {
            
            System.out.println(e.getMessage());
            throw new Exception("Erro ao buscar dados");
        }
    }

}
