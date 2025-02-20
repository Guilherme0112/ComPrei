package com.example.loja.controllers;

import com.example.loja.exceptions.TokenException;
import com.example.loja.service.VerificationEmailService;
import jakarta.validation.constraints.Email;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class EmailController {

    private final VerificationEmailService verificationEmailService;

    public EmailController(VerificationEmailService verificationEmailService){
        this.verificationEmailService = verificationEmailService;
    }


    @GetMapping("/email/confirmation/{token}")
    public ModelAndView validationEmail(@PathVariable("token") String token) throws Exception, TokenException {

        ModelAndView mv = new ModelAndView();

        try {

            // Lógica para verificar a autenticidade do token
            if(verificationEmailService.isValidToken(token)){
                throw new TokenException("O token é inválido");
            }

            if(verificationEmailService.isExpired(token)){
                throw new TokenException("O token está expirado");
            }

            // Método que faz a verificação de todos os tokens do banco de dados
            verificationEmailService.verificationTokens();

            mv.setViewName("redirect:/auth/login");

        } catch (TokenException e){

            throw new TokenException(e.getMessage());

        } catch (Exception e) {

            throw new Exception(e.getMessage());
        }

        return mv;
    }
}
