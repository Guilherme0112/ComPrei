package com.example.loja.service;

import java.util.List;

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

            // Verifica se o produto é nulo
            if(produto == null) throw new ProdutoException("Ocorreu algum erro. Tente novamente mais tarde");
            
            // Pega o codigo de barras do produto
            String codigo = produto.getCodigo();

            // Verifica se o código de barras contém apenas números
            if(!codigo.matches("\\d+")) throw new ProdutoException("Somente números são válidos");
            
            // Verifica se o produto ja foi cadastrado com o mesmo codigo
            Pageable pageable = PageRequest.of(0, 1);
            if(!produtoRepository.findByCodigoDeBarras(codigo, pageable).isEmpty()){

                // Verifica se o produto tem a mesma rota da foto, pois o método que faz o build do produto sempre coloca
                // um nome aleatório na foto (Se for igual é porque é o sistema que ta salvando mais registros do produto, se não é um 
                // registro novo)
                if(!produto.getPhoto().equals(produtoRepository.findByCodigoDeBarras(codigo, pageable).get(0).getPhoto())){

                    throw new ProdutoException("Este código já está cadastrado");
                }
            }

            // Salva o produto
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

    /** Busca produtos pelo nome dele
     *  
     * @param nome Nome do produto
     * @return  Retorna uma lista de produtos
     * @throws Exception Erro genérico
     * @throws ProdutoException Erros relacionados ao produto
     */
    public List<Produto> searchProduto(String query) throws Exception {

        try {
            
            return produtoRepository.searchResults(query);

        } catch (Exception e) {
            
            throw new Exception(e.getMessage());
        }
    }
}
