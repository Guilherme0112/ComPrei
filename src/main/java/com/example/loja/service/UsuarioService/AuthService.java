package com.example.loja.service.UsuarioService;

import java.util.ArrayList;
import java.util.List;

import com.example.loja.exceptions.UsuarioException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final ProfileService profileService;

    public AuthService(UsuarioRepository usuarioRepository,
                       HttpServletRequest request,
                       ProfileService profileService) {
        this.usuarioRepository = usuarioRepository;
        this.profileService = profileService;
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
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
            SecurityContextHolder.getContext().setAuthentication(authToken);
            HttpSession http = request.getSession(true);
            http.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());


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

            // V alida a senha do usuário
            if(usuario.getPassword().length() < 6 || usuario.getPassword().length() > 10){

               throw new UsuarioException("A senha deve ter entre 6 e 10 caracteres"); 
            }

            // Verifica se o e-mail está já está sendo usado
            if (!usuarioRepository.findByEmail(usuario.getEmail(), true).isEmpty()) {

                throw new UsuarioException("Este e-mail já está sendo usado");
            }

            // Caso o usuário já tenha o e-mail registrado, mas a conta está inativa
            if(!usuarioRepository.findByEmail(usuario.getEmail(), false).isEmpty()){

                // Método para ativar a conta
                profileService.activeAccount(usuario);
                return;
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
    public Usuario getSession(HttpSession http) throws SessionException{
        try {
            // Obtém a autenticação do SecurityContextHolder
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // Verifica se o usuário está autenticado corretamente
            if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
                throw new SessionException("O usuário não tem permissão");
            }

            // Obtém o objeto do usuário autenticado
            Usuario user = (Usuario) authentication.getPrincipal();

            return user;

        } catch (SessionException e) {
            throw new SessionException(e.getMessage());

        }

    }
}
