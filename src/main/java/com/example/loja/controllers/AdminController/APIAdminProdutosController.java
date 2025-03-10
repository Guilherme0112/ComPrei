package com.example.loja.controllers.AdminController;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.loja.exceptions.ProdutoException;
import com.example.loja.models.Produto;
import com.example.loja.repositories.ProdutoRepository;

@RestController
public class APIAdminProdutosController {

    private final ProdutoRepository produtoRepository;

    public APIAdminProdutosController(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;

    }

    @GetMapping("/admin/produtos/{codigo}")
    public List<?> AdminProdutosGET(@PathVariable("codigo") String codigo) {

        try {

            if (!codigo.matches("\\d+")) {
                return List.of("erro", "O código  é inválido");
            }

            return produtoRepository.findByCodigoDeBarras(codigo);

        } catch (Exception e) {

            return List.of("erro", e.getMessage());
        }

    }

    @DeleteMapping("/admin/deletar/produtos")
    public List<?> DeletarProduto(@RequestBody Map<String, String> codigoMap) throws Exception, ProdutoException {
        try {

            String codigo = codigoMap.get("codigo");

            if (!codigo.matches("\\d+")) {
                return List.of("erro", "O código  é inválido");
            }

            if (produtoRepository.findByCodigoDeBarras(codigo).isEmpty()) {
                throw new ProdutoException("Produto não existe");
            }

            Produto produto = produtoRepository.findByCodigoDeBarras(codigo).get(0);

            produtoRepository.delete(produto);

            return List.of(200, "Produto deletado com sucesso");

        } catch (ProdutoException e) {

            return List.of("erro", e.getMessage());

        } catch (Exception e) {

            System.out.println(e.getMessage());
            return List.of("erro", "Ocorreu algum erro. Tente novamente mais tarde");
        }
    }

}
