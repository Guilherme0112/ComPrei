package com.example.loja.controllers;

import com.example.loja.exceptions.UsuarioException;
import com.example.loja.models.VerificationEmail;
import com.example.loja.repositories.VerificationEmailRepository;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.loja.exceptions.SessionException;
import com.example.loja.models.Usuario;
import com.example.loja.models.dto.LoginRequest;
import com.example.loja.service.AuthService;
import com.example.loja.service.EmailService;
import com.example.loja.util.Util;

import jakarta.validation.Valid;

@Controller
public class AuthController {

    private final AuthService authService;
    private final EmailService emailService;
    private final VerificationEmailRepository verificationEmailRepository;

    public AuthController(AuthService authService,
                          EmailService emailService,
                          VerificationEmailRepository verificationEmailRepository) {
        this.authService = authService;
        this.emailService = emailService;
        this.verificationEmailRepository = verificationEmailRepository;
    }

    @GetMapping("/auth/login")
    public ModelAndView Login(LoginRequest loginRequest) {

        ModelAndView mv = new ModelAndView();

        mv.setViewName("/views/auth/login");

        return mv;
    }

    @PostMapping("auth/login")
    public ModelAndView LoginPOST(LoginRequest loginRequest) throws Exception, SessionException {

        ModelAndView mv = new ModelAndView();

        try {

            // Chamada do service para validar e criar sessão
            authService.createSession(loginRequest);

        } catch (SessionException e) {

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
    public ModelAndView Register() {

        ModelAndView mv = new ModelAndView();

        mv.addObject("usuario", new Usuario());
        mv.setViewName("/views/auth/register");

        return mv;
    }

    @PostMapping("/auth/register")
    public ModelAndView RegisterPOST(@Valid Usuario usuario, BindingResult br) throws Exception, UsuarioException {

        ModelAndView mv = new ModelAndView();

        try {

            // Caso houver erros, os retorna
            if (br.hasErrors()) {
                mv.setViewName("/views/auth/register");
                mv.addObject("usuario", usuario);
                return mv;
            }

            // Tenta criar a conta do usuário
            authService.createUser(usuario);

            // Cria o token
            String token = Util.generateToken();

            // HTML que será enviado para o usuario
            String html = """
                    <!DOCTYPEhtml>
                    <html>
                    <head>
                    <metacharset="UTF-8">
                    <metaname="viewport"content="width=device-width,initial-scale=1.0">
                    <title>Confirmação de E-mail</title>
                    <style>
                    body{
                    font-family: Arial ,sans-serif;
                    background-color: #f4f4f4;
                    text-align: center;
                    padding: 20px;
                    }
                    .container{
                    background: #ffffff;
                    padding: 20px;
                    border-radius: 10px;
                    box-shadow: 0px 0px 10px rgba(0,0,0,0.1);
                    max-width: 500px;
                    margin: auto;
                    }
                    .button{
                    display: inline-block;
                    padding: 10px 20px;
                    color: white;
                    background-color: #007BFF;
                    text-decoration: none;
                    border-radius: 5px;
                    font-size: 16px;
                    margin-top: 20px;
                    }
                    .footer{
                    margin-top: 20px;
                    font-size: 12px;
                    color: #777;
                    }
                    </style>
                    </head>
                    <body>
                    <div class="container">
                    <h2>Confirme seu e-mail</h2>
                    <p>Obrigado por se cadastrar! Clique no botão abaixo para confirmar seu e-mail.</p>
                    <a href="http://127.0.0.1:8080/email/confirmation/%s" class="button">Confirmar E-mail</a>
                    <p class="footer">Se você não se cadastrou, ignore este e-mail.</p>
                    </div>
                    </body>
                    </html>""".formatted(token);

            // Envia o e-mail de verificação
            emailService.sendEmail(usuario.getEmail(), "Confirmação de e-mail", html);

            // Salva o token no banco de dados para a verificação
            VerificationEmail email_request_token = new VerificationEmail();
            email_request_token.setEmail(usuario.getEmail());
            email_request_token.setToken(token);
            verificationEmailRepository.save(email_request_token);

            mv.setViewName("/views/mails/send_email");
        } catch (UsuarioException e){

            mv.addObject("erro", e.getMessage());
            mv.setViewName("/views/auth/register");

        } catch (Exception e) {

            System.out.println("auth_controller: " + e.getMessage());
            mv.addObject("erro", "Ocorreu algum erro interno. Tente novamente mais tarde");
            mv.setViewName("/views/auth/register");
        }

        return mv;
    }

}
