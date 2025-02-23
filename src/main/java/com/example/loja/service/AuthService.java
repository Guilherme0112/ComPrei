package com.example.loja.service;

import java.util.List;

import com.example.loja.exceptions.UsuarioException;
import jakarta.servlet.http.HttpServletRequest;
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
    private final HttpServletRequest request;

    public AuthService(UsuarioRepository usuarioRepository,
                       HttpServletRequest request) {
        this.usuarioRepository = usuarioRepository;

        this.request = request;
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
            if (existEmail.isEmpty()) {
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
            HttpSession http = request.getSession(true);
            http.setAttribute("session", email);

        } catch (SessionException e) {

            throw new SessionException(e.getMessage());

        } catch (Exception e) {

            System.out.println("auth_service: " + e.getMessage());
            throw new Exception(e.getMessage());
        }

    }

    /***
     * Cria o registro do usuário no banco de dados
     * 
     * @param usuario Objeto do usuário 
     * @throws Exception Erros de execução e etc...
     */
    public void createUser(Usuario usuario) throws Exception, UsuarioException {

        try {


            // Verifica se o e-mail está já está sendo usado
            if (!usuarioRepository.findByEmail(usuario.getEmail(), true).isEmpty()) {
                throw new UsuarioException("Este e-mail já está sendo usado");
            }

            // Caso o usuário já tenha o e-mail registrado, mas a conta está inativa
            if(!usuarioRepository.findByEmail(usuario.getEmail(), false).isEmpty()){

                // Lógica para fazer a ativação da conta
            }

            // Criptografa a senha do usuário
            usuario.setPassword(Util.Bcrypt(usuario.getPassword()));

            usuarioRepository.save(usuario);

        } catch (UsuarioException e){

            throw new UsuarioException(e.getMessage());

        } catch (Exception e) {

            System.out.println("auth_service: " + e.getMessage());
            throw new Exception("Ocorreu algum erro. Tente novamente mais tarde");
        }
    }

    /***
     * Método que verifica a existência de uma sessão
     *
     * @param http Onde será verificada se existe a sessão
     * @return Retorna o objeto do usuário caso exista a sessão
     * @throws SessionException Caso não exista sessão
     */
    public Usuario getSession(HttpSession http) throws SessionException, Exception{
        try {

            Object user = http.getAttribute("session");

            // Verifica a existência da sessão
            if(user == null){
                throw new SessionException("O usuário não tem permissão");
            }

            // Converte o email para String e busca no banco de dados
            String user_email = user.toString();
            List<Usuario> userList = usuarioRepository.findByEmail(user_email, true);

            // Verifica se o dono da sessão está cadastrado
            if(userList.isEmpty()){
                throw new SessionException("Esta sessão é inválida");
            }

            return userList.get(0);

        } catch (SessionException e) {

            throw new SessionException(e.getMessage());

        } catch (Exception e) {

            throw new Exception(e.getMessage());
        }
    }
}
