package com.example.loja.controllers;

import com.example.loja.exceptions.SessionException;
import com.example.loja.models.Usuario;
import com.example.loja.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ProfileController {

    private final AuthService authService;

    public ProfileController(AuthService authService){
        this.authService = authService;
    }

    @GetMapping("/profile")
    public ModelAndView Profile(HttpSession httpSession) throws SessionException, Exception {

        ModelAndView mv = new ModelAndView();

        try {

            // Tenta buscar o usuário da sessão
            Usuario user = authService.getSession(httpSession);

            mv.addObject(user);
            mv.setViewName("/views/profile/profile");

        } catch (SessionException e) {

            mv.setViewName("redirect:/auth/login");
            System.out.println("session_exception: " + e.getMessage());

        } catch (Exception e) {

            System.out.println("exception: " + e.getMessage());
            mv.setViewName("redirect:/auth/login");
        }

        return mv;
    }

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
