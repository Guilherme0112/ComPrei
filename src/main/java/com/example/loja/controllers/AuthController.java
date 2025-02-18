package com.example.loja.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.loja.exceptions.SessionException;
import com.example.loja.models.dto.LoginRequest;
import com.example.loja.service.AuthService;


@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    
    @GetMapping("/auth/login")
    public ModelAndView Login(LoginRequest loginRequest){

        ModelAndView mv = new ModelAndView();

        mv.setViewName("/views/auth/login");

        return mv;
    }

    @PostMapping("auth/login")
    public ModelAndView LoginPOST(LoginRequest loginRequest) throws Exception, SessionException{

        ModelAndView mv = new ModelAndView();

        try {

            // Chamada do service para validar e criar sessão
            authService.createSession(loginRequest);

        } catch (SessionException e){

            // Retorna o erro para a view
            mv.addObject("erro", e.getMessage());
            mv.setViewName("/views/auth/login");

        } catch (Exception e) {
            // Retorna o erro para a view
            System.out.println(e.getMessage());
            mv.addObject("erro", "Ocorreu um erro. Tente novamente mais tarde");
            mv.setViewName("/views/auth/login");
        }
        

        return mv;
    }

    @GetMapping("/auth/register")
    public ModelAndView Register(){

        ModelAndView mv = new ModelAndView();

        mv.setViewName("/views/auth/register");

        return mv;
    }


   
}
