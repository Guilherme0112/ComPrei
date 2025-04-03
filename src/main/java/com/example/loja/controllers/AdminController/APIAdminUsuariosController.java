package com.example.loja.controllers.AdminController;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.loja.enums.Cargo;
import com.example.loja.exceptions.UsuarioException;
import com.example.loja.models.Usuario;
import com.example.loja.repositories.UsuarioRepository;

@RestController
public class APIAdminUsuariosController {

    private final UsuarioRepository usuarioRepository;

    public APIAdminUsuariosController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;

    }

    @GetMapping("/admin/usuarios/{id}")
    public List<?> AdminUsuariosAPIGET(@PathVariable("id") String id) {
        try {

            if (!id.matches("\\d+")) {
                return List.of("erro", "O código  é inválido");
            }

            Long idLong = Long.parseLong(id);

            Optional<Usuario> usuario = usuarioRepository.findById(idLong);

            return usuario.map(Collections::singletonList)
                    .orElseGet(Collections::emptyList);

        } catch (Exception e) {

            return List.of("erro", e.getMessage());
        }
    }

    @GetMapping("/admin/usuarios/banir/{id}")
    public List<?> BanirOrDesbanir(@PathVariable("id") String id) throws Exception, UsuarioException {

        try {

            if (!id.matches("\\d+")) {
                return List.of("erro", "O código  é inválido");
            }

            Long idLong = Long.parseLong(id);

            if (usuarioRepository.findById(idLong).isEmpty()) {
                throw new UsuarioException("Usuário não existe");
            }

            Usuario user = usuarioRepository.findById(idLong).get();

            if (user.getRole().equals(Cargo.ROLE_BANIDO)) {

                user.setRole(Cargo.ROLE_CLIENTE);
                usuarioRepository.save(user);
                return List.of(200, "Usuário desbanido com sucesso");
            }

            user.setRole(Cargo.ROLE_BANIDO);
            usuarioRepository.save(user);

            return List.of(200, "Usuário banido com sucesso");

        } catch (UsuarioException e) {

            System.out.println(e.getMessage());
            return List.of("erro", e.getMessage());

        } catch (Exception e) {

            System.out.println(e.getMessage());
            return List.of("erro", "Ocorreu algum erro. Tente novamente mais tarde");
        }
    }

    @GetMapping("/admin/usuarios/admin/{id}")
    public List<?> SetAdmin(@PathVariable("id") String id) throws Exception, UsuarioException {

        try {

            if (!id.matches("\\d+")) {
                return List.of("erro", "O código  é inválido");
            }

            Long idLong = Long.parseLong(id);

            if (usuarioRepository.findById(idLong).isEmpty()) {
                throw new UsuarioException("Usuário não existe");
            }

            Usuario user = usuarioRepository.findById(idLong).get();

            if (user.getRole().equals(Cargo.ROLE_BANIDO)) {
                return List.of("erro", "Você precisa desbanir o usuário primeiro");
            }

            if (user.getRole().equals(Cargo.ROLE_CLIENTE)) {

                user.setRole(Cargo.ROLE_ADMIN);
                usuarioRepository.save(user);
                return List.of(200, "O usuário agora é admin");
            } else {
                
                user.setRole(Cargo.ROLE_CLIENTE);
                usuarioRepository.save(user);
                return List.of(200, "Você retirou o cargo de admin do id " + id);
            }

        } catch (UsuarioException e) {

            System.out.println(e.getMessage());
            return List.of("erro", e.getMessage());

        } catch (Exception e) {

            System.out.println(e.getMessage());
            return List.of("erro", "Ocorreu algum erro. Tente novamente mais tarde");
        }
    }
}
