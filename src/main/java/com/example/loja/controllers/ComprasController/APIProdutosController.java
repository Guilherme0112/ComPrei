package com.example.loja.controllers.ComprasController;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.loja.exceptions.ProdutoException;
import com.example.loja.models.Produto;
import com.example.loja.models.dto.ProdutoDTO;
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

            // Busca os 15 produtos de acordo com o indice
            int indice = Integer.parseInt(i) * 15;
            
            // Cria o pageable para buscar de acordo com o indice
            Pageable pageable = PageRequest.of(indice, 15);

            // Retorna o DTO com a lista de produtos
            return new ProdutoPageDTO(produtoRepository.findRandom(pageable));

        } catch (Exception e) {
            
            System.out.println(e.getMessage());
            throw new Exception("Erro ao buscar dados");
        }
    }

    @PostMapping("/carrinho/produtos")
    public List<?> verifyCar(@RequestBody ProdutoDTO codigos) throws Exception, ProdutoException{

        try {

            // Verifica se existe produtos npo carrinho
            if(codigos.getProdutos().isEmpty()) throw new ProdutoException("O carrinho está vazio");

            // Cria a array de retorno e pega a array com produtos
            List<String> codigosList = codigos.getProdutos();
            List<Produto> res = new ArrayList<>();

            // Busca somente o primeiro registro de cada query
            Pageable pageable = PageRequest.of(0, 1);

            // Busca todos os produtos do carrinho e coloca na resposta
            for (String produto : codigosList) {

                if(!produtoRepository.findByCodigoDeBarras(produto, pageable).isEmpty()){
                    res.add(
                        produtoRepository.findByCodigoDeBarras(produto, pageable).get(0)
                    );
                }
            }
            
            return res; 

        } catch (ProdutoException e){

            return List.of("erro", e.getMessage()); 

        } catch (Exception e) {

            System.out.println(e.getMessage());
            return List.of("erro", "Ocorreu algum erro. Tente novamente mais tarde"); 
        }
        
    }

}
