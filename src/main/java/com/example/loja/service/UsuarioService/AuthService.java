package com.example.loja.service.UsuarioService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.loja.enums.Cargo;
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
            if (email == null || senha == null) throw new SessionException("Você deve preencher todos os campos");
            
            // Busca no banco de dados uma conta com o email do usuario e se esta conta
            // está ativa
            Usuario userBD = usuarioRepository.findByEmail(email, true).stream()
                                                                                  .findFirst()
                                                                                  .orElseThrow(() -> new SessionException("As credenciais está incorretas"));

            // Verifica se está banida
            if (userBD.getRole().equals(Cargo.ROLE_BANIDO)) throw new SessionException("Esta conta está banida");

            // Verifica se as senhas correspondem
            if (!Util.verifyPass(senha, userBD.getPassword())) {
                throw new SessionException("As credenciais estão incorretas");
            }

            // Cria a sessão caso passe por toda a validação
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                                                                        userBD,
                                                                            null,
                                                                                        userBD.getAuthorities()
                                                                    );

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
     * Método que verifica a existência de uma sessão
     *
     * @param http Onde será verificada se existe a sessão
     * @return Retorna o objeto do usuário caso exista a sessão
     * @throws SessionException Caso não exista sessão
     */
    public Usuario buscarSessaoUsuario() throws Exception, SessionException {
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

        } catch (Exception e) {

            throw new Exception(e.getMessage());
        }

    }
}
