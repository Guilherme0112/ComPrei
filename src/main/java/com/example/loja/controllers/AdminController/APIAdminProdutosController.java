package com.example.loja.controllers.AdminController;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.example.loja.exceptions.ProdutoException;
import com.example.loja.models.Produto;
import com.example.loja.repositories.ProdutoRepository;
import com.example.loja.service.ProdutoService;
import com.example.loja.service.AdminService.AdminProdutosService;

import jakarta.validation.Valid;

@RestController
public class APIAdminProdutosController {

    private final ProdutoRepository produtoRepository;
    private final AdminProdutosService adminProdutosService;
    private final ProdutoService produtoService;

    public APIAdminProdutosController(ProdutoRepository produtoRepository,
            AdminProdutosService adminProdutosService,
            ProdutoService produtoService) {
        this.produtoRepository = produtoRepository;
        this.adminProdutosService = adminProdutosService;
        this.produtoService = produtoService;

    }

    @GetMapping("/admin/produtos/{codigo}")
    public ResponseEntity<?> AdminAPIProdutosGET(@PathVariable("codigo") String codigo)
            throws Exception, ProdutoException {

        try {

            if (!codigo.matches("\\d+")) throw new ProdutoException("O código é inválido");

            // Busca a quantidade de registros que tem no banco de dados
            Long amountP = produtoRepository.countByCodigo(codigo);

            // Retorna uma Lista com o objeto do produto e a quantidade
            return ResponseEntity.ok().body(
                List.of(
                    produtoService.getProduct(codigo),
                    amountP.toString()
                )
            );

        } catch (ProdutoException e) {

            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (Exception e) {

            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body("Ocorreu algum erro. Tente novamente mais tarde");
        }

    }

    @PostMapping("/admin/produtos/criar")
    public ModelAndView CriarProduto(@Valid Produto produto,
            BindingResult br,
            @RequestParam String amount,
            @RequestParam MultipartFile file) throws Exception, ProdutoException {

        ModelAndView mv = new ModelAndView();

        try {

            if (br.hasErrors()) {
                mv.addObject(produto);
                mv.setViewName("views/admin/produtos/criarProdutos");
                return mv;
            }

            // Cria o produto
            adminProdutosService.createProduct(produto, file, amount);

            mv.setViewName("redirect:/admin/produtos");

        } catch (ProdutoException e) {

            System.out.println("api_controller: " + e.getMessage());
            mv.addObject("erro", e.getMessage());
            mv.setViewName("views/admin/produtos/criarProdutos");

        } catch (Exception e) {

            System.out.println("api_controller: " + e.getMessage());
            mv.addObject("erro", "Ocorreu algum erro. Tente novamente mais tarde");
            mv.setViewName("views/admin/produtos/criarProdutos");
        }

        return mv;
    }

    @DeleteMapping("/admin/deletar/produtos")
    public List<?> DeletarProduto(@RequestBody String codigo) throws Exception, ProdutoException {
        try {

            // Remove as aspas
            codigo = codigo.replaceAll("^\"|\"$", "");

            // Válida o codigo para garantir que omente seja composto apenas por números
            // números
            if (!codigo.matches("\\d+"))
                throw new ProdutoException("O código é inválido");

            adminProdutosService.deleteProduct(codigo);

            return List.of(HttpStatus.OK);

        } catch (ProdutoException e) {

            return List.of("erro", e.getMessage());

        } catch (Exception e) {

            System.out.println(e.getMessage());
            return List.of("erro", "Ocorreu algum erro. Tente novamente mais tarde");
        }
    }

}
