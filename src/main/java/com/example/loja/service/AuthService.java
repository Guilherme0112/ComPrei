package com.example.loja.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.example.loja.exceptions.SessionException;
import com.example.loja.models.Usuario;
import com.example.loja.models.dto.LoginRequest;
import com.example.loja.repositories.UsuarioRepository;
import com.example.loja.util.Util;

import jakarta.servlet.http.HttpSession;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final HttpSession http;

    public AuthService(UsuarioRepository usuarioRepository,
                        HttpSession http) {
        this.usuarioRepository = usuarioRepository;
        this.http = http;
    }

    /***
     * Valida as credenciais para garantir que o usuário somente consiga fazer a
     * sessão se estiver correto
     * 
     * @param loginRequest DTO (Data Transfer Object) das credenciais (email e
     *                     senha)
     * @throws Exception Caso haja algum erro de validação e etc...
     */

    public void createSession(LoginRequest loginRequest) throws Exception, SessionException {

        try {

            // Login e senha do formulário
            String email = loginRequest.getEmail();
            String senha = loginRequest.getSenha();

            // Verifica se estão vazios
            if (email == null || senha == null) {
                throw new SessionException("Você deve preencher todos os campos");
            }

            // Busca no banco de dados uma conta com o email do usuario e se esta conta
            // está ativa
            List<Usuario> existEmail = usuarioRepository.findByEmail(email, true);

            // Verifica o resultado do banco de dados
            if (existEmail.size() == 0) {
                throw new SessionException("As credenciais estão incorretas");
            }

            // Obtém o objeto do usuario e a senha
            Usuario user = existEmail.get(0);
            String passwordBD = user.getPassword();

            // Verifica se as senhas correspondem
            if(!Util.verifyPass(senha, passwordBD)){
                throw new SessionException("As credenciais estão incorretas");
            }

            // Cria a sessão caso passe por toda a validação
           http.setAttribute("session", email);

        } catch (SessionException e) {

            throw new SessionException(e.getMessage());

        } catch (Exception e) {

            System.out.println("erro_exception: " + e.getMessage());
            throw new Exception(e.getMessage());
        }

    }

    /***
     * Cria o registro do usuário no banco de dados
     * 
     * @param usuario Objeto do usuário 
     * @throws Exception Erros de execução e etc...
     */
    public void createUser(Usuario usuario) throws Exception{

        try {
            
            // Verifica se o usuário de fato existe
            if(usuario == null){
                new Exception("Ocorreu algum erro. Tente novamente mais tarde");
            }

           usuarioRepository.save(usuario);

        } catch (Exception e) {

            System.out.println("erro_exception: " + e.getMessage());
            throw new Exception(e.getMessage());
        }
    }
}
