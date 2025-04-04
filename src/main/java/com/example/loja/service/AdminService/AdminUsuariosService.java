package com.example.loja.service.AdminService;

import org.springframework.stereotype.Service;

import com.example.loja.enums.Cargo;
import com.example.loja.exceptions.UsuarioException;
import com.example.loja.models.Usuario;
import com.example.loja.repositories.UsuarioRepository;

@Service
public class AdminUsuariosService {

    private final UsuarioRepository usuarioRepository;

    public AdminUsuariosService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /** Bane o usuário se ele for um usuário comum e desbane se
     * tive banido
     * 
     * @param user Objeto do usuário que será verificado
     * @throws Exception Erro genérico
     */
    public void BanirOuDesbanir(Usuario user) throws Exception {

        try {

            // Se o usuário tiver banido, ele tira o banimento
            if (user.getRole().equals(Cargo.ROLE_BANIDO)) {

                user.setRole(Cargo.ROLE_CLIENTE);
                usuarioRepository.save(user);
                return;
            }

            // Se não tiver banido, ele bane
            user.setRole(Cargo.ROLE_BANIDO);
            usuarioRepository.save(user);

        } catch (Exception e) {

            throw new Exception(e.getMessage());
        }
    }

    /** Seta o usuário como admin e retira se já for admin
     * 
     * @param user Objeto do usuário
     * @throws Exception Erro genérico
     */
    public void AdminOuNao(Usuario user) throws Exception, UsuarioException {

        try {   

            // Verifica se o usuario está banido
            if (user.getRole().equals(Cargo.ROLE_BANIDO)) {
                throw new UsuarioException("Você precisa desbanir o usuário primeiro");
            }

            // Se ele for cliente, muda para admin
            if (user.getRole().equals(Cargo.ROLE_CLIENTE)) {

                user.setRole(Cargo.ROLE_ADMIN);
                usuarioRepository.save(user);

            } else {
                // Se ele for admin, muda para cliente
                user.setRole(Cargo.ROLE_CLIENTE);
                usuarioRepository.save(user);
            }

        } catch (UsuarioException e) {
            
            throw new UsuarioException(e.getMessage());
            
        } catch (Exception e) {
            
            throw new Exception(e.getMessage());
        }
    }
}
