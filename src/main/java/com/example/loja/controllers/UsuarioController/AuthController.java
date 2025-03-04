package com.example.loja.controllers.UsuarioController;

import com.example.loja.exceptions.UsuarioException;
import com.example.loja.models.VerificationEmail;
import com.example.loja.repositories.EmailRequestRepository;
import com.example.loja.repositories.VerificationEmailRepository;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.loja.exceptions.EmailRequestException;
import com.example.loja.exceptions.SessionException;
import com.example.loja.models.EmailRequests;
import com.example.loja.models.Usuario;
import com.example.loja.models.dto.LoginRequest;
import com.example.loja.service.EmailsService.EmailRequestService;
import com.example.loja.service.EmailsService.EmailService;
import com.example.loja.service.UsuarioService.AuthService;
import com.example.loja.util.Util;

import jakarta.validation.Valid;

@Controller
public class AuthController {

    private final AuthService authService;
    private final EmailService emailService;
    private final VerificationEmailRepository verificationEmailRepository;
    private final EmailRequestRepository emailRequestRepository;
    private final EmailRequestService emailRequestService;

    public AuthController(AuthService authService,
                          EmailService emailService,
                          VerificationEmailRepository verificationEmailRepository,
                          EmailRequestRepository emailRequestRepository,
                          EmailRequestService emailRequestService) {
        this.authService = authService;
        this.emailService = emailService;
        this.verificationEmailRepository = verificationEmailRepository;
        this.emailRequestRepository = emailRequestRepository;
        this.emailRequestService = emailRequestService;
    }

    @GetMapping("/auth/login")
    public ModelAndView Login(LoginRequest loginRequest) {

        ModelAndView mv = new ModelAndView();

        mv.setViewName("/views/auth/login");

        return mv;
    }

    @PostMapping("/auth/login")
    public ModelAndView LoginPOST(LoginRequest loginRequest) throws Exception, SessionException {

        ModelAndView mv = new ModelAndView();

        try {

            // Chamada do service para validar e criar sessão
            authService.createSession(loginRequest);

            mv.setViewName("redirect:/profile");

        } catch (SessionException e) {

            // Retorna o erro para a view
            System.out.println("session_exception" + e.getMessage());
            mv.addObject("erro", e.getMessage());
            mv.setViewName("/views/auth/login");

        } catch (Exception e) {
            // Retorna o erro para a view
            System.out.println("exception: " + e.getMessage());
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
    public ModelAndView RegisterPOST(@Valid Usuario usuario, BindingResult br) throws Exception, UsuarioException, EmailRequestException {

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

            // Verifica se o usuário pediu um email a menos de 2 minutos
            if(!emailRequestService.verifyUserRequest(usuario)){
                throw new EmailRequestException("Ocorreu algum erro. tente novamente mais tarde");
            }

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

            // Salva a requisição no banco de dados
            EmailRequests email_request = new EmailRequests();
            email_request.setEmail(usuario.getEmail());
            emailRequestRepository.save(email_request);

            // Salva o token no banco de dados para a verificação
            VerificationEmail email_request_token = new VerificationEmail(usuario.getEmail(), token);
            verificationEmailRepository.save(email_request_token);

            mv.setViewName("/views/mails/send_email");

        } catch (EmailRequestException e){

            System.out.println("email_request_exception: " + e.getMessage());
            mv.addObject("erro", e.getMessage());
            mv.setViewName("/views/auth/register");

        } catch (UsuarioException e){

            System.out.println("usuario_exception: " + e.getMessage());
            mv.addObject("erro", e.getMessage());
            mv.setViewName("/views/auth/register");

        } catch (Exception e) {

            System.out.println("exception: " + e.getMessage());
            mv.addObject("erro", "Ocorreu algum erro interno. Tente novamente mais tarde");
            mv.setViewName("/views/auth/register");
        }

        return mv;
    }

}
