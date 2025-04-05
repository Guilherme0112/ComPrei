package com.example.loja.controllers.UsuarioController;

import com.example.loja.exceptions.UsuarioException;
import com.example.loja.models.VerificationEmail;
import com.example.loja.repositories.VerificationEmailRepository;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.loja.exceptions.EmailRequestException;
import com.example.loja.exceptions.SessionException;
import com.example.loja.models.Usuario;
import com.example.loja.models.dto.LoginRequest;
import com.example.loja.service.EmailsService.EmailRequestService;
import com.example.loja.service.EmailsService.EmailService;
import com.example.loja.service.EmailsService.LoadTemplatesService;
import com.example.loja.service.UsuarioService.AuthService;
import com.example.loja.service.UsuarioService.ProfileService;
import com.example.loja.util.Util;

import jakarta.validation.Valid;

@Controller
public class AuthController {

    private final AuthService authService;
    private final EmailService emailService;
    private final VerificationEmailRepository verificationEmailRepository;
    private final EmailRequestService emailRequestService;
    private final LoadTemplatesService loadTemplatesService;
    private final ProfileService profileService;

    public AuthController(AuthService authService,
                          EmailService emailService,
                          VerificationEmailRepository verificationEmailRepository,
                          EmailRequestService emailRequestService,
                          LoadTemplatesService loadTemplatesService,
                          ProfileService profileService) {
        this.authService = authService;
        this.emailService = emailService;
        this.verificationEmailRepository = verificationEmailRepository;
        this.emailRequestService = emailRequestService;
        this.loadTemplatesService = loadTemplatesService;
        this.profileService = profileService;
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
    public synchronized ModelAndView RegisterPOST(@Valid Usuario usuario, BindingResult br) throws Exception, UsuarioException, EmailRequestException {

        ModelAndView mv = new ModelAndView();

        try {

            // Caso houver erros, os retorna
            if (br.hasErrors()) {
                mv.setViewName("/views/auth/register");
                mv.addObject("usuario", usuario);
                return mv;
            }

            // Tenta criar a conta do usuário
            profileService.createUser(usuario);

            // Cria o token
            String token = Util.generateToken();

            // Verifica se o usuário pediu um email a menos de 1 minuto
            emailRequestService.verifyUserRequest(usuario.getEmail());

            // Envia o e-mail de verificação
            emailService.sendEmail(
                usuario.getEmail(),
                "Confirmação de e-mail",
                loadTemplatesService.welcome(token)    
            );

            // Salva a requisição no banco de dados
            emailRequestService.saveRequestEmail(usuario.getEmail());

            // Salva o token no banco de dados para a verificação
            verificationEmailRepository.save(
                new VerificationEmail(
                    usuario.getEmail(),
                    token
                )
            );

            mv.setViewName("/views/mails/send_email");

        } catch (UsuarioException | EmailRequestException e){

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
