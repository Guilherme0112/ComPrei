package com.example.loja.controllers.UsuarioController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class EditUsuarioController {
    
    @GetMapping("/profile/edit")
    public ModelAndView EditUserGET(){

        ModelAndView mv = new ModelAndView();

        return mv;
    }

    @PostMapping("/profile/edit")
    public ModelAndView EditUserPOST(){
        
        ModelAndView mv = new ModelAndView();

        return mv;
    }
}
