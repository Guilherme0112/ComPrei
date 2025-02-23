package com.example.loja.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
    
    @GetMapping("/")
    public ModelAndView Home(){

        ModelAndView mv = new ModelAndView();

        mv.setViewName("views/index");

        return mv;
    }

}
