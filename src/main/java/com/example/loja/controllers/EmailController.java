package com.example.loja.controllers;

import com.example.loja.exceptions.TokenException;
import com.example.loja.repositories.VerificationEmailRepository;
import com.example.loja.service.VerificationEmailService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class EmailController {

    private final VerificationEmailService verificationEmailService;
    private final VerificationEmailRepository verificationEmailRepository;

    public EmailController(VerificationEmailService verificationEmailService,
                           VerificationEmailRepository verificationEmailRepository) {
        this.verificationEmailService = verificationEmailService;
        this.verificationEmailRepository = verificationEmailRepository;
    }


    @GetMapping("/email/confirmation/{token}")
    public ModelAndView validationEmail(@PathVariable("token") String token) throws Exception, TokenException {

        ModelAndView mv = new ModelAndView();

        try {

            // Valida o token ou lança uma exceção caso não exista
            verificationEmailService.validationEmailUser(
                verificationEmailRepository.findByToken(token).stream()
                                                              .findFirst()
                                                              .orElseThrow(() -> new TokenException("Token não encontrado")
                )
            );          

            // Método que faz a verificação de todos os tokens do banco de dados
            verificationEmailService.verificationTokens();
        
            mv.setViewName("redirect:/auth/login");

        } catch (TokenException e){

            System.out.println("token_exception: " + e.getMessage());
            throw new TokenException(e.getMessage());

        } catch (Exception e) {

            System.out.println("exception: " + e.getMessage());
            throw new Exception(e.getMessage());
        }

        return mv;
    }
}
