package com.example.loja.service.UsuarioService;

import org.springframework.stereotype.Service;

import com.example.loja.exceptions.PasswordException;
import com.example.loja.models.Usuario;
import com.example.loja.models.dto.PasswordRequest;
import com.example.loja.repositories.UsuarioRepository;
import com.example.loja.util.Util;

@Service
public class ProfileService {
    
    private final UsuarioRepository usuarioRepository;

    public ProfileService(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
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
}
