package com.example.loja.controllers;

import com.example.loja.exceptions.TokenException;
import com.example.loja.models.Usuario;
import com.example.loja.models.VerificationEmail;
import com.example.loja.repositories.UsuarioRepository;
import com.example.loja.repositories.VerificationEmailRepository;
import com.example.loja.service.VerificationEmailService;
import jakarta.validation.constraints.Email;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class EmailController {

    private final VerificationEmailService verificationEmailService;
    private final VerificationEmailRepository verificationEmailRepository;
    private final UsuarioRepository usuarioRepository;

    public EmailController(VerificationEmailService verificationEmailService,
                           VerificationEmailRepository verificationEmailRepository,
                           UsuarioRepository usuarioRepository){
        this.verificationEmailService = verificationEmailService;
        this.verificationEmailRepository = verificationEmailRepository;
        this.usuarioRepository = usuarioRepository;
    }


    @GetMapping("/email/confirmation/{token}")
    public ModelAndView validationEmail(@PathVariable("token") String token) throws Exception, TokenException {

        ModelAndView mv = new ModelAndView();

        try {

            // Lógica para verificar a autenticidade do token
            if(verificationEmailRepository.findByToken(token).isEmpty()){

                // Deleta o token do banco de dados
                List<VerificationEmail> tokenList = verificationEmailRepository.findByToken(token);
                VerificationEmail tokenObj = tokenList.get(0);
                verificationEmailRepository.delete(tokenObj);

                throw new TokenException("O token é inválido");
            }

            if(verificationEmailService.isExpired(token)){

                // Deleta o token do banco de dados
                List<VerificationEmail> tokenList = verificationEmailRepository.findByToken(token);
                VerificationEmail tokenObj = tokenList.get(0);
                verificationEmailRepository.delete(tokenObj);

                throw new TokenException("O token está expirado");
            }

            // Busca o email que é dono do token
            List<VerificationEmail> tokenList = verificationEmailRepository.findByToken(token);
            VerificationEmail tokenObj = tokenList.get(0);

            usuarioRepository.changeActive(true, tokenObj.getEmail());

            // Método que faz a verificação de todos os tokens do banco de dados
            verificationEmailService.verificationTokens();

            // Deleta o token
            verificationEmailRepository.delete(tokenObj);

            mv.setViewName("redirect:/auth/login");

        } catch (TokenException e){

            throw new TokenException(e.getMessage());

        } catch (Exception e) {

            throw new Exception(e.getMessage());
        }

        return mv;
    }
}
