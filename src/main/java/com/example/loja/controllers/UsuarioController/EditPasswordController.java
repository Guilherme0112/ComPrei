package com.example.loja.controllers.UsuarioController;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.loja.exceptions.EmailRequestException;
import com.example.loja.exceptions.PasswordException;
import com.example.loja.exceptions.TokenException;
import com.example.loja.models.ResetPassword;
import com.example.loja.models.Usuario;
import com.example.loja.models.dto.PasswordRequest;
import com.example.loja.repositories.ResetPasswordRepository;
import com.example.loja.repositories.UsuarioRepository;
import com.example.loja.service.EmailsService.EmailRequestService;
import com.example.loja.service.EmailsService.EmailService;
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

    public EditPasswordController(AuthService authService,
            EditPasswordService editPasswordService,
            EmailRequestService emailRequestService,
            UsuarioRepository usuarioRepository,
            EmailService emailService,
            ResetPasswordRepository resetPasswordRepository,
            ResetPasswordService resetPasswordService) {
        this.authService = authService;
        this.editPasswordService = editPasswordService;
        this.emailRequestService = emailRequestService;
        this.usuarioRepository = usuarioRepository;
        this.emailService = emailService;
        this.resetPasswordRepository = resetPasswordRepository;
        this.resetPasswordService = resetPasswordService;
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
            Usuario user = authService.buscarSessaUsuario();

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
            if (email.isEmpty()) {
                throw new EmailRequestException("O email é obrigatório");
            }

            // Verifica se o email está cadastrado no banco de dados
            if (usuarioRepository.findByEmail(email, true).isEmpty()) {
                throw new EmailRequestException("Este e-mail não está cadastrado ou está inativo");
            }

            // Verifica as requisições do usuário
            emailRequestService.verifyUserRequest(email);

            String token = Util.generateToken();

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
                    <h2>Redefinição de senha</h2>
                    <p>Para redefinir sua senha clique no botão.</p>
                    <a href="http://127.0.0.1:8080/reset/password/user/%s" class="button">Redefinir Senha</a>
                    <p class="footer">Se você não pediu para redefinir a senha, ignore este e-mail.</p>
                    </div>
                    </body>
                    </html>""".formatted(token);

            emailService.sendEmail(email, "Redefinição de senha", html);

            // Salva uma requisição se o usuario não tiver requisições
            emailRequestService.saveRequestEmail(email);

            // Salva o pedido de troca de senha, salvando um token e um email
            ResetPassword reset_password_request = new ResetPassword(token, email);
            resetPasswordRepository.save(reset_password_request);

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
    public ModelAndView ResetSenhaGET(@PathVariable("token") String token) throws Exception, TokenException{

        ModelAndView mv = new ModelAndView();

        try {

            // Verifica todos os tokens
            resetPasswordService.verifyAllTokens();

            // Busca o token atual
            List<ResetPassword> verify = resetPasswordRepository.findByToken(token);

            // Verifica se existe um token
            if(verify.isEmpty()){
                throw new TokenException("Token inválido");
            }

            // Busca o usuário que pediu o token
            List<Usuario> user = usuarioRepository.findByEmail(verify.get(0).getEmail(), true);
            if(user.isEmpty()){
                throw new Exception("Erro ao encontar usuário");
            }

            // Gera uma nova senha
            String novaSenha = Util.generateSenha();
            
            // Atualiza a senha
            Usuario userObj = user.get(0);
            userObj.setPassword(Util.Bcrypt(novaSenha));
            usuarioRepository.save(userObj);

            // Deleta o token  
            resetPasswordRepository.delete(verify.get(0));

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
                    <h2>Redefinição de senha</h2>
                    <p>Você acabou de redefinir sua senha, não a guarde em lugares que não tenha segurança.</p>
                    <p class="footer">Se não foi você que redefiniu a senha, <a href='http://127.0.0.1:8080/reset/password'>clique aqui</a> para recuperar sua conta.</p>
                    </div>
                    </body>
                    </html>""".formatted(token);

            emailService.sendEmail(userObj.getEmail(), "Redefinição de Senha", html);

            mv.addObject("success","Sua senha foi alterada para " + novaSenha + ", acesse sua conta e faça a redefinição da senha");
            mv.setViewName("views/mails/alterPassword");
            
            
        } catch(TokenException e){

            mv.addObject("erro", e.getMessage());
            mv.setViewName("views/mails/alterPassword");

        } catch (Exception e) {
            
            mv.addObject("erro", "Ocorreu algum erro interno. Tente novamente mais tarde");
            mv.setViewName("views/mails/alterPassword");
        }      

        return mv;
    }
}
