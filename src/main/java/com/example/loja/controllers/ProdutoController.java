package com.example.loja.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ProdutoController {
    
    @GetMapping("/profile/carrinho")
    public ModelAndView Carrinho(){

        ModelAndView mv = new ModelAndView();
        mv.setViewName("views/profile/carrinho/carrinho");
        return mv;
    }

    @GetMapping("/profile/favoritos")
    public ModelAndView Favoritos(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("views/profile/favoritos/favoritos");
        return mv;
    }
}
