package com.example.loja.controllers;

import com.example.loja.exceptions.SessionException;
import com.example.loja.models.Usuario;
import com.example.loja.models.UsuarioAddress;
import com.example.loja.service.AuthService;
import com.example.loja.service.UsuarioAddressService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ProfileController {

    private final AuthService authService;
    private final UsuarioAddressService usuarioAddressService;

    public ProfileController(AuthService authService,
                             UsuarioAddressService usuarioAddressService){
        this.authService = authService;
        this.usuarioAddressService = usuarioAddressService;
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

    @GetMapping("/profile/edit/address")
    public ModelAndView Config(){

        ModelAndView mv = new ModelAndView();

        mv.addObject("address", new UsuarioAddress());
        mv.setViewName("views/profile/address");

        return mv;
    }

    @PostMapping("/profile/edit/address")
    public ModelAndView ConfigPost(@Valid UsuarioAddress usuarioAddress, BindingResult br, HttpSession http) throws Exception{

        ModelAndView mv = new ModelAndView();

        try {
            
            if(br.hasErrors()){
                mv.addObject("address", usuarioAddress);
                mv.setViewName("views/profile/address");
                return mv;
            }

            // Chama o service pegando o email da sessão
            usuarioAddressService.saveAddress(usuarioAddress, authService.getSession(http).getEmail());

            mv.setViewName("redirect:/profile");

        } catch (Exception e) {
            
            System.out.println("exception: " + e.getMessage());
            mv.setViewName("views/profile/address");
            mv.addObject("erro", "Ocorreu algum erro interno. Tente novamente mais tarde");
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
