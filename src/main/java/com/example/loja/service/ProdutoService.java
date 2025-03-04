package com.example.loja.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.loja.exceptions.ProdutoException;
import com.example.loja.models.Produto;
import com.example.loja.repositories.ProdutoRepository;

@Service
public class ProdutoService {
    
    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository){
        this.produtoRepository = produtoRepository;
    }

    /***
     * Cria o registro do produto
     * 
     * @param produto Objeto do produto que será registrado
     * @throws ProdutoException Caso algum dado do objeto estiver inválido
     * @throws Exception Erros do sistema
     */
    public void createProduct(Produto produto) throws ProdutoException, Exception{

        try {

            if(produto == null){
                throw new ProdutoException("Ocorreu algum erro. Tente novamente mais tarde");
            }

            produtoRepository.save(produto);

        } catch(ProdutoException e){

            System.out.println("produto_service: " + e.getMessage());
            throw new ProdutoException(e.getMessage());

        } catch (Exception e) {

            System.out.println("produto_service: " + e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    /***
     * Retorna o produto com o id do parametro
     * 
     * @param id Id do produto
     * @return Retorna o objeto do produto
     * @throws ProdutoException Caso o produto não existe e erros afins
     * @throws Exception Erros do sistema
     */
    public Produto getProduct(Long id) throws ProdutoException, Exception{
        try {
            
            Optional<Produto> productOpt = produtoRepository.findById(id);

            if(productOpt.isEmpty()){
                throw new ProdutoException("O id do produto é inválido");
            }

            Produto produtoObj = productOpt.get();

            if(produtoObj.getAmount() == 0){
                throw new ProdutoException("O produto está sem estoque");
            }

            return produtoObj;

        } catch (ProdutoException e){

            throw new ProdutoException(e.getMessage());

        } catch (Exception e) {

            throw new Exception(e.getMessage());
        }
    }
}
