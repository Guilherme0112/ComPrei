package com.example.loja.service.AdminService;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.loja.exceptions.ProdutoException;
import com.example.loja.models.Produto;
import com.example.loja.service.ProdutoService;
import com.example.loja.util.Util;

@Service
public class AdminProdutosService {

    private static final String UPLOAD_DIR = "/uploads/";

    private final ProdutoService produtoService;

    public AdminProdutosService(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    public void createProduct(Produto produto, MultipartFile file, String amount) throws Exception, ProdutoException {

        try {

            // Verifica se o arquivo existe
            if (file.isEmpty()) {
                throw new ProdutoException("A foto é obrigatório");
            }

            // Verifica se a quantidade é um número válido
            if (!amount.matches("\\d+")) {
                throw new ProdutoException("A quantidade deve ser um valor válido");
            }

            // Se for um número válido, converte para Integer e verifica se o número está no intervalo válido
            int amountInt = Integer.parseInt(amount);
            if (amountInt > 10000 || amountInt < 1) {
                throw new ProdutoException("A quantidade não pode ser maior que 10000 ou menor que 1");
            }

            // Cria e verifica o diretório de onde o arquivo será salvo
            File dir = new File(UPLOAD_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Formata o nome do arquivo
            String fileName = file.getOriginalFilename().replaceAll(" ", "");
            File serverFile = new File(
                    "/app/" + dir.getAbsolutePath() + File.separator + Util.generateToken() + "_" + fileName);
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));

            // Salva o arquivo no diretório
            stream.write(file.getBytes());
            stream.close();

            // Salva no banco de dados
            produto.setPhoto(serverFile.toString().replaceFirst("/app", ""));           

            // Salva os produtos
            for (int i = 0; i < amountInt; i++) {

                Produto produto2 = new Produto();
                produto2.setCodigo(produto.getCodigo());
                produto2.setDescription(produto.getDescription());
                produto2.setName(produto.getName());
                produto2.setPhoto(produto.getPhoto());
                produto2.setPrice(produto.getPrice());

                produtoService.createProduct(produto2);
            }

        } catch(ProdutoException e){

            throw new ProdutoException(e.getMessage());

        } catch (Exception e) {

            throw new Exception(e.getMessage());
        }
    }
}
