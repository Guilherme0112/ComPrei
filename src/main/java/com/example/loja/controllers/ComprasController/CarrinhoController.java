package com.example.loja.controllers.ComprasController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CarrinhoController {
    
    @GetMapping("/profile/carrinho")
    public ModelAndView Carrinho(){

        ModelAndView mv = new ModelAndView();
        mv.setViewName("views/profile/carrinho/carrinho");
        return mv;
    }
}
