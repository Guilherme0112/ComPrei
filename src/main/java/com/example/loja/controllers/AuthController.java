package com.example.loja.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.loja.models.dto.LoginRequest;

@Controller
public class AuthController {
    
    @GetMapping("/auth/login")
    public ModelAndView Login(LoginRequest loginRequest){

        ModelAndView mv = new ModelAndView();

        mv.setViewName("/views/auth/login");

        return mv;
    }

    @GetMapping("/auth/register")
    public ModelAndView Register(){

        ModelAndView mv = new ModelAndView();

        mv.setViewName("/views/auth/register");

        return mv;
    }


   
}
