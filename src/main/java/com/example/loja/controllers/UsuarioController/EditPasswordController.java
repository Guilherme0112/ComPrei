package com.example.loja.controllers.UsuarioController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.loja.exceptions.EmailRequestException;
import com.example.loja.exceptions.PasswordException;
import com.example.loja.exceptions.TokenException;
import com.example.loja.exceptions.UsuarioException;
import com.example.loja.models.ResetPassword;
import com.example.loja.models.Usuario;
import com.example.loja.models.dto.PasswordRequest;
import com.example.loja.repositories.ResetPasswordRepository;
import com.example.loja.repositories.UsuarioRepository;
import com.example.loja.service.EmailsService.EmailRequestService;
import com.example.loja.service.EmailsService.EmailService;
import com.example.loja.service.EmailsService.LoadTemplatesService;
import com.example.loja.service.UsuarioService.AuthService;
import com.example.loja.service.UsuarioService.EditPasswordService;
import com.example.loja.service.UsuarioService.ResetPasswordService;
import com.example.loja.util.Util;

@Controller
public class EditPasswordController {

    private final AuthService authService;
    private final EditPasswordService editPasswordService;
    private final EmailRequestService emailRequestService;
    private final UsuarioRepository usuarioRepository;
    private final EmailService emailService;
    private final ResetPasswordRepository resetPasswordRepository;
    private final ResetPasswordService resetPasswordService;
    private final LoadTemplatesService loadTemplatesService;

    public EditPasswordController(AuthService authService,
            EditPasswordService editPasswordService,
            EmailRequestService emailRequestService,
            UsuarioRepository usuarioRepository,
            EmailService emailService,
            ResetPasswordRepository resetPasswordRepository,
            ResetPasswordService resetPasswordService,
            LoadTemplatesService loadTemplatesService) {
        this.authService = authService;
        this.editPasswordService = editPasswordService;
        this.emailRequestService = emailRequestService;
        this.usuarioRepository = usuarioRepository;
        this.emailService = emailService;
        this.resetPasswordRepository = resetPasswordRepository;
        this.resetPasswordService = resetPasswordService;
        this.loadTemplatesService = loadTemplatesService;
    }

    @GetMapping("/profile/edit/password")
    public ModelAndView TrocarSenha() {

        ModelAndView mv = new ModelAndView();

        mv.addObject("passwordRequest", new PasswordRequest());
        mv.setViewName("views/profile/edit/password");

        return mv;
    }

    @PostMapping("/profile/edit/password")
    public ModelAndView TrocarSenhaPOST(PasswordRequest passwordRequest)
            throws Exception, PasswordException {

        ModelAndView mv = new ModelAndView();

        try {

            // Pegando o objeto do usuário da sessão
            Usuario user = authService.buscarSessaoUsuario();

            // Verifica a sessão do usuário
            if (user == null) {
                mv.addObject("erro", "Ocorreu algum erro. Tente novamente mais tarde");
                mv.setViewName("views/profile/edit/password");
                return mv;
            }

            // Verifica se a senha corresponde com a do banco
            if (!Util.verifyPass(passwordRequest.getSenhaAntiga(), user.getPassword())) {

                mv.setViewName("views/profile/edit/password");
                mv.addObject("erro", "A senha está incorreta");
                return mv;
            }

            // Altera a senha
            editPasswordService.alterPassword(passwordRequest, user);

            // Redireciona para /profile
            mv.setViewName("redirect:/profile");

        } catch (PasswordException e) {

            System.out.println(e.getMessage());
            mv.setViewName("views/profile/edit/password");
            mv.addObject("erro", e.getMessage());

        } catch (Exception e) {

            System.out.println(e.getMessage());
            mv.setViewName("views/profile/edit/password");
            mv.addObject("erro", "Ocorreu algum erro. Tente novamente mais tarde");
        }

        return mv;
    }

    @GetMapping("/reset/password")
    public ModelAndView ResetSenhaPorEmailGET() {

        ModelAndView mv = new ModelAndView();

        mv.setViewName("views/profile/edit/reset-password-email");

        return mv;
    }

    @PostMapping("/reset/password")
    public synchronized ModelAndView ResetSenhaPorEmailPOST(@RequestParam String email) throws Exception, EmailRequestException {

        ModelAndView mv = new ModelAndView();
        
        try {

            // Verifica se o usuário preencheu o email
            if (email.isEmpty()) throw new EmailRequestException("O email é obrigatório");
            
            // Verifica se o email está cadastrado no banco de dados
            if (usuarioRepository.findByEmail(email, true).isEmpty()) throw new EmailRequestException("Este e-mail não está cadastrado ou está inativo");
                
            // Verifica as requisições do usuário
            emailRequestService.verifyUserRequest(email);

            // Gera o token 
            String token = Util.generateToken();
            
            // Envia o email
            emailService.sendEmail(
                email,
                "Redefinição de senha",
                loadTemplatesService.resetSenha(token)
            );

            // Salva uma requisição se o usuario não tiver requisições
            emailRequestService.saveRequestEmail(email);

            // Salva o pedido de troca de senha, salvando um token e um email
            resetPasswordRepository.save(
                new ResetPassword(token, email)
            );

            mv.addObject("success", "Enviamos um e-mail para você redefinir sua senha");
            mv.setViewName("views/profile/edit/reset-password-email");

        } catch (EmailRequestException e) {

            mv.addObject("erro", e.getMessage());
            mv.setViewName("views/profile/edit/reset-password-email");

        } catch (Exception e) {

            mv.addObject("erro", "Ocorreu algum erro. Tente novamente mais tarde");
            mv.setViewName("views/profile/edit/reset-password-email");
        }

        return mv;
    }

    @GetMapping("/reset/password/user/{token}")
    public ModelAndView ResetSenhaGET(@PathVariable("token") String token) throws Exception, TokenException, UsuarioException{

        ModelAndView mv = new ModelAndView();

        try {

            // Verifica todos os tokens
            resetPasswordService.verifyAllTokens();

            // Busca o token atual e verfica se ele existe
            ResetPassword verify = resetPasswordRepository.findByToken(token).stream()
                                                                            .findFirst()                                                                            
                                                                            .orElseThrow(() -> new TokenException("Token inválido"));
                
            // Busca o usuário que pediu o token
            Usuario user = usuarioRepository.findByEmail(verify.getEmail(), true).stream()
                                                                                        .findFirst()
                                                                                        .orElseThrow(() -> new UsuarioException("Usuário não encotrado"));

            // Gera uma nova senha
            String novaSenha = Util.generateSenha(8);
            
            // Atualiza a senha
            user.setPassword(Util.Bcrypt(novaSenha));
            usuarioRepository.save(user);

            // Deleta o token  
            resetPasswordRepository.delete(verify);

            // Envia o email
            emailService.sendEmail(
                user.getEmail(), 
        "Redefinição de Senha", 
                loadTemplatesService.avisoDeTrocaDeSenha()
            );

            mv.addObject("success","Sua senha foi alterada para " + novaSenha + ", acesse sua conta e faça a redefinição da senha");
            mv.setViewName("views/mails/alterPassword");
            
            
        } catch(TokenException | UsuarioException e){

            mv.addObject("erro", e.getMessage());
            mv.setViewName("views/mails/alterPassword");

        } catch (Exception e) {
            
            mv.addObject("erro", "Ocorreu algum erro interno. Tente novamente mais tarde");
            mv.setViewName("views/mails/alterPassword");
        }      

        return mv;
    }
}
