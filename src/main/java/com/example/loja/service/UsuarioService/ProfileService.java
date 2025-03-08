package com.example.loja.service.UsuarioService;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.loja.exceptions.EmailRequestException;
import com.example.loja.exceptions.PasswordException;
import com.example.loja.exceptions.UsuarioException;
import com.example.loja.models.Usuario;
import com.example.loja.models.VerificationEmail;
import com.example.loja.models.dto.PasswordRequest;
import com.example.loja.repositories.UsuarioRepository;
import com.example.loja.repositories.VerificationEmailRepository;
import com.example.loja.service.EmailsService.EmailRequestService;
import com.example.loja.service.EmailsService.EmailService;
import com.example.loja.util.Util;

@Service
public class ProfileService {
    
    private final UsuarioRepository usuarioRepository;
    private final EmailService emailService;
    private final EmailRequestService emailRequestService;
    private final VerificationEmailRepository verificationEmailRepository;

    public ProfileService(UsuarioRepository usuarioRepository,
                          EmailService emailService,
                          EmailRequestService emailRequestService,
                          VerificationEmailRepository verificationEmailRepository){
        this.usuarioRepository = usuarioRepository;
        this.emailService = emailService;
        this.emailRequestService = emailRequestService;
        this.verificationEmailRepository = verificationEmailRepository;

    }

    /***
     * Método que altera a senha do usuário
     * 
     * @param passwordRequest DTO das senhas do usuario (Senha nova e Senha antiga)
     * @param usuario Objeto do usuário que terá a senha trocada
     * @throws PasswordException Erro de validação da senha 
     * @throws Exception Erro genérico
     */
    public void alterPassword(PasswordRequest passwordRequest, Usuario usuario) throws Exception, PasswordException{

        try {
            
            // Senha nova do usuário
            String antigaSenha = passwordRequest.getSenhaAntiga();
            String novaSenha = passwordRequest.getSenhaNova();

            if(antigaSenha.length() == 0){
                throw new PasswordException("Preencha todos os campos");                
            }

            // Valida a senha nova
            if(novaSenha.length() < 6 || novaSenha.length() > 10){
                throw new PasswordException("A senha deve ter entre 6 e 10");
            }

            // Criptografando a senha e salvando no banco de dados
            usuario.setPassword(Util.Bcrypt(novaSenha));
            usuarioRepository.save(usuario);

        } catch(PasswordException e){

            System.out.println("profile_service: " + e.getMessage());
            throw new PasswordException(e.getMessage());

        } catch (Exception e) {
            
            System.out.println("profile_service: " + e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    /***
     * Deleta o registro do usuário do banco de dados
     * 
     * @param user Objeto do usuário que será deletado
     * @throws UsuarioException Erros referentes ao usuário
     * @throws Exception Erro genérico
     */
    public void deleteUser(Usuario user) throws Exception, UsuarioException {
        try{

            List<Usuario> userVerify = usuarioRepository.findByEmail(user.getEmail(), true);

            // Verifica se a conta existe
            if(userVerify.isEmpty()){
                throw new UsuarioException("Erro ao apagar conta. Tente novamente mais tarde");
            }

            // Deleta a conta
            usuarioRepository.delete(userVerify.get(0));

        } catch (UsuarioException e) {

            System.out.println("profile_service: " + e.getMessage());
            throw new UsuarioException(e.getMessage());

        } catch (Exception e) {

            System.out.println("profile_service: " + e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    /***
     * Ativa a conta do usuário que a tem inativa
     * 
     * @param user Objeto do usuário que tem a conta inativa
     * @throws Exception Erro genérico
     * @throws UsuarioException Erros relacionados ao usuário
     */
    public synchronized void activeAccount(Usuario user) throws Exception, UsuarioException, EmailRequestException{
        
        try {
            
        

            if(user.getActive() == true){

                throw new UsuarioException("Esta conta já está ativa");
            }

            // Verifica as requisições do usuário
            emailRequestService.verifyUserRequest(user);

            // Cria o token
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
                        <h2>Confirme seu e-mail</h2>
                        <p>Obrigado por se cadastrar! Clique no botão abaixo para confirmar seu e-mail.</p>
                        <a href="http://127.0.0.1:8080/email/confirmation/%s" class="button">Confirmar E-mail</a>
                        <p class="footer">Se você não se cadastrou, ignore este e-mail.</p>
                        </div>
                        </body>
                        </html>""".formatted(token);

                // Envia o e-mail de verificação
                emailService.sendEmail(user.getEmail(), "Confirmação de e-mail", html);

                // Salva a requisição no banco de dados
                emailRequestService.saveRequestEmail(user.getEmail());

                // Salva o token no banco de dados para a verificação
                VerificationEmail email_request_token = new VerificationEmail(user.getEmail(), token);
                verificationEmailRepository.save(email_request_token);

            } catch (EmailRequestException e) {

                System.out.println("profile_service: " + e.getMessage());
                throw new EmailRequestException(e.getMessage());
            
            } catch (UsuarioException e) {

                System.out.println("profile_service: " + e.getMessage());
                throw new UsuarioException(e.getMessage());
        
            } catch (Exception e) {
                System.out.println("profile_service: " + e.getMessage());
                throw new Exception(e.getMessage());
            }
    }
}
