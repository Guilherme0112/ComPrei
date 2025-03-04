package com.example.loja.controllers.ComprasController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.example.loja.exceptions.ProdutoException;
import com.example.loja.models.Produto;
import com.example.loja.service.ProdutoService;

@Controller
public class ProdutoController {
    
    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService){
        this.produtoService = produtoService;
    }


    @GetMapping("/produto/{id}")
    public ModelAndView Produto(@PathVariable("id") String id) throws Exception, ProdutoException{

        ModelAndView mv = new ModelAndView();

        try {
            
            Long id_long = Long.parseLong(id);

            Produto produto = produtoService.getProduct(id_long);

            mv.addObject(produto);
            
        } catch (ProdutoException e){

            mv.addObject("erro", e.getMessage());
    
        } catch (Exception e) {

    
            System.out.println(e.getMessage());
            mv.addObject("erro", "Ocorreu um erro. Tente novamente mais tarde");
        }
        
        mv.setViewName("/views/produto/produto");
        return mv;
    }

}
