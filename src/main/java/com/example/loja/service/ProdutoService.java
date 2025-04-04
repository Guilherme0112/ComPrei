package com.example.loja.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

            String codigo = produto.getCodigo();

            if(!codigo.matches("\\d+")){
                throw new ProdutoException("Somente números são válidos");
            }
            Pageable pageable = PageRequest.of(0, 1);
            if(!produtoRepository.findByCodigoDeBarras(codigo, pageable).isEmpty()){

                if(!produto.getName().equals(produtoRepository.findByCodigoDeBarras(codigo, pageable ).get(0).getName())){
                    throw new ProdutoException("Este código já está cadastrado");
                }

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
    public Produto getProduct(String codigo) throws ProdutoException, Exception{
        try {
            
            // Cria o PageAble que retorna o primeiro resultado
            Pageable pageable = PageRequest.of(0, 1);

            // Retorna o primeiro resultado e lança uma exceção caso não houver dados
            return produtoRepository.findByCodigoDeBarras(codigo, pageable).stream()
                                                                           .findFirst()
                                                                           .orElseThrow(() -> new ProdutoException("O produto nao foi encontrado"));
        } catch (ProdutoException e){

            throw new ProdutoException(e.getMessage());

        } catch (Exception e) {

            throw new Exception(e.getMessage());
        }
    }

    /**
     * Deleta um produto do banco de dados
     * 
     * @param produto Objeto do produto que será deletado
     * @throws Exception Erros genéricos
     * @throws ProdutoException Erros referente ao produto
     */
    public void deleteProduct(Produto produto) throws Exception, ProdutoException{

        try {

            if(produto == null){
                throw new ProdutoException("Produto não existe");
            }
            
            produtoRepository.delete(produto);

        } catch (ProdutoException e) {
            throw new ProdutoException(e.getMessage());
    
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }
}
