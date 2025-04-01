package com.example.loja.controllers.ComprasController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.loja.exceptions.SessionException;
import com.example.loja.repositories.ReembolsosRepository;
import com.example.loja.service.UsuarioService.AuthService;

import jakarta.servlet.http.HttpSession;

@RestController
public class ReembolsoController {

    private final AuthService authService;
    private final ReembolsosRepository reembolsoRepository;

    public ReembolsoController(AuthService authService,
                               ReembolsosRepository reembolsoRepository) {
        this.authService = authService;
        this.reembolsoRepository = reembolsoRepository;
    }

    @GetMapping("/profile/reembolsos")
    public ModelAndView reembolso(HttpSession http) throws SessionException, Exception {

        ModelAndView mv = new ModelAndView();

        try {


            String email = authService.getSession(http).getEmail();

            mv.setViewName("views/produto/pedido/reembolso");
            mv.addObject("reembolsos", reembolsoRepository.findByEmail(email));

        } catch (Exception e) {

            mv.setViewName("redirect:/profile");
        }

        return mv;
    }

    
}
