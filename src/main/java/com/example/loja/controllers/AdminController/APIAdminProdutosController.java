package com.example.loja.controllers.AdminController;

import java.util.List;
import java.util.Map;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.loja.exceptions.ProdutoException;
import com.example.loja.models.Produto;
import com.example.loja.repositories.ProdutoRepository;
import com.example.loja.service.ProdutoService;

import jakarta.validation.Valid;

@RestController
public class APIAdminProdutosController {

    private final ProdutoRepository produtoRepository;
    private final ProdutoService produtoService;

    public APIAdminProdutosController(ProdutoRepository produtoRepository,
                                      ProdutoService produtoService) {
        this.produtoRepository = produtoRepository;
        this.produtoService = produtoService;

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

    @PostMapping("/admin/produtos/criar")
    public ModelAndView CriarProduto(@Valid Produto produto, BindingResult br) throws Exception, ProdutoException{

        ModelAndView mv = new ModelAndView();

        try {

            if(br.hasErrors()){
                mv.addObject("produtos", produto);
                mv.setViewName("views/admin/criarProdutos");
                return mv;
            }
            
            produtoService.createProduct(produto);
            mv.setViewName("redirect:/admin/produtos");

        } catch(ProdutoException e){

            System.out.println("api_controller: " + e.getMessage());
            mv.addObject("erro", e.getMessage());
            mv.setViewName("views/admin/criarProdutos");

        } catch (Exception e) {

            System.out.println("api_controller: " + e.getMessage());
            mv.addObject("erro", "Ocorreu algum erro. Tente novamente mais tarde");
            mv.setViewName("views/admin/criarProdutos");
        }

        return mv;
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
