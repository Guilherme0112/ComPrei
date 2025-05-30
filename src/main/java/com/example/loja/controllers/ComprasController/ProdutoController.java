package com.example.loja.controllers.ComprasController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.example.loja.exceptions.ProdutoException;
import com.example.loja.models.Produto;
import com.example.loja.repositories.ProdutoRepository;
import com.example.loja.service.ProdutoService;

@Controller
public class ProdutoController {
    
    private final ProdutoService produtoService;
    private final ProdutoRepository produtoRepository;

    public ProdutoController(ProdutoService produtoService,
                             ProdutoRepository produtoRepository) {
        this.produtoService = produtoService;
        this.produtoRepository = produtoRepository;
    }


    @GetMapping("/produto/{codigo}")
    public ModelAndView Produto(@PathVariable("codigo") String codigo) throws Exception, ProdutoException{

        ModelAndView mv = new ModelAndView();

        try {
            

            // Guarda o valor das variáveis
            Produto produto = produtoService.getProduct(codigo);
            Long amount = produtoRepository.countByCodigo(codigo);

            // Retorna os valores para a view 
            mv.addObject(produto);
            mv.addObject("quantidade", amount);
            mv.setViewName("views/produto/produto");
            
        } catch (ProdutoException e){

            System.out.println(e.getMessage());
            mv.setViewName("redirect:/");
    
        } catch (Exception e) {

            System.out.println(e.getMessage());
            mv.addObject("erro", "Ocorreu um erro. Tente novamente mais tarde");
            mv.setViewName("redirect:/");
        }
        
        return mv;
    }
}
