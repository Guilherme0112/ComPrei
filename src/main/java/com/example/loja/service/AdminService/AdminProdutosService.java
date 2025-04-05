package com.example.loja.service.AdminService;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.loja.exceptions.ProdutoException;
import com.example.loja.models.Produto;
import com.example.loja.repositories.ProdutoRepository;
import com.example.loja.service.ProdutoService;
import com.example.loja.util.Util;

@Service
public class AdminProdutosService {

    private static final String UPLOAD_DIR = "/uploads/";

    private final ProdutoService produtoService;
    private final ProdutoRepository produtoRepository;

    public AdminProdutosService(ProdutoService produtoService,
            ProdutoRepository produtoRepository) {
        this.produtoService = produtoService;
        this.produtoRepository = produtoRepository;
    }

    /** Cria o produto no banco de dados
     * 
     * @param produto Objeto do produto que será salvo
     * @param file Arquivo de imagem do produto
     * @param amount Quantidade total dos produtos
     * @throws Exception Erro genéricos
     * @throws ProdutoException Erros relacionados ao produto
     */
    public void createProduct(Produto produto, MultipartFile file, String amount) throws Exception, ProdutoException {

        try {

            // Verifica se o arquivo existe
            if (file.isEmpty()) throw new ProdutoException("A foto é obrigatório");

            // Verifica se a quantidade é um número válido
            if (!amount.matches("\\d+")) throw new ProdutoException("A quantidade deve ser um valor válido");

            // Se for um número válido, converte para Integer e verifica se o número está no
            // intervalo válido
            int amountInt = Integer.parseInt(amount);
            if (amountInt > 10000 || amountInt < 1) throw new ProdutoException("A quantidade não pode ser maior que 10000 ou menor que 1");
        
            // Cria e verifica o diretório de onde o arquivo será salvo
            File dir = new File(UPLOAD_DIR);
            if (!dir.exists()) dir.mkdirs();
            
            // Formata o nome do arquivo
            File serverFile = new File(
                    "/app/" + dir.getAbsolutePath() + File.separator + Util.generateToken() + "_" + Util.generateSenha()
            );

            // Inicia a escrita do arquivo
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));

            // Salva o arquivo no diretório
            stream.write(file.getBytes());
            stream.close();

            // Salva no banco de dados
            produto.setPhoto(serverFile.toString().replaceFirst("/app", ""));

            // Salva os produtos
            for (int i = 0; i < amountInt; i++) {

                produtoService.createProduct(
                    new Produto(
                        produto.getCodigo(),
                        produto.getName(),
                        produto.getDescription(),
                        produto.getPrice(),
                        produto.getPhoto()
                    )
                );
            }

        } catch (ProdutoException e) {

            throw new ProdutoException(e.getMessage());

        } catch (Exception e) {

            throw new Exception(e.getMessage());
        }
    }

    /** Deleta o produto do banco de dados
     * 
     * @param codigo Código de barras do produto
     * @throws Exception Erros genéricos
     * @throws ProdutoException Erros relacionados ao produto
     */
    public void deleteProduct(String codigo) throws Exception, ProdutoException {

        try {

            // Pega somente o primeiro resultado da query
            Pageable pageable = PageRequest.of(0, 1);

            // Busca o produto no banco de dados ou lança uma exceção caso não exista
            Produto produto = produtoRepository.findByCodigoDeBarras(codigo, pageable).stream()
                                                                                      .findFirst()
                                                                                      .orElseThrow(() -> new ProdutoException("Produto não encontrado"));

            // Verifica se a foto do produto existe e o deleta caso exista                                                          
            Files.deleteIfExists(
                Paths.get("/app" + produto.getPhoto())
            );

            // Deleta o produto do banco de dados
            produtoRepository.deleteByCodigo(produto.getCodigo());

        } catch (ProdutoException e) {

            throw e;

        } catch (Exception e) {

            throw new Exception(e.getMessage());
        }
    }
}
