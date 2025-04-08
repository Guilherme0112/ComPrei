package com.example.loja.controllers.AdminController;

import java.util.Collections;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.loja.exceptions.UsuarioException;
import com.example.loja.models.Usuario;
import com.example.loja.repositories.UsuarioRepository;
import com.example.loja.service.AdminService.AdminUsuariosService;
import com.mercadopago.net.HttpStatus;

@RestController
public class APIAdminUsuariosController {

    private final UsuarioRepository usuarioRepository;
    private final AdminUsuariosService adminUsuariosService;

    public APIAdminUsuariosController(UsuarioRepository usuarioRepository,
            AdminUsuariosService adminUsuariosService) {
        this.usuarioRepository = usuarioRepository;
        this.adminUsuariosService = adminUsuariosService;

    }

    @GetMapping("/admin/usuarios/{usuario}")
    public ResponseEntity<?> AdminUsuariosAPIGET(@PathVariable("usuario") String usuario) throws Exception, UsuarioException {
        try {

            // Se for apenas números, ele busca por id
            if (usuario.matches("\\d+")) {

                // Pega o usuário como Optional e converte para List
                Long idLong = Long.parseLong(usuario);
                return usuarioRepository.findById(idLong)
                                        .map(user -> ResponseEntity.ok(Collections.singletonList(user)))
                                        .orElseThrow(() -> new UsuarioException("Usuário não encontrado"));
            } else {

                return usuarioRepository.findByEmail(usuario, true).stream()
                                                                          .findFirst()
                                                                          .map(user -> ResponseEntity.ok(Collections.singletonList(user)))
                                                                          .orElseThrow(() -> new UsuarioException("Usuário não encontrado"));
            }

        } catch (UsuarioException e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/admin/usuarios/banir")
    public List<?> BanirOrDesbanir(@RequestBody String usuario) throws Exception, UsuarioException {

        try {

            // Verifica se existem apenas números na string
            usuario = usuario.replaceAll("^\"|\"$", "");
            if (!usuario.matches("\\d+"))
                throw new UsuarioException("O código é inválido");

            // Busca o usuário ou lança uma exceção caso não encontre
            Usuario user = usuarioRepository.findById(Long.parseLong(usuario)).stream()
                    .findFirst()
                    .orElseThrow(() -> new UsuarioException("Usuário não encontrado"));

            // Chama o método que bane se tiver desbanido e desbane se tiver banido
            adminUsuariosService.BanirOuDesbanir(user);

            return List.of(HttpStatus.OK);

        } catch (UsuarioException e) {

            System.out.println(e.getMessage());
            return List.of(HttpStatus.BAD_REQUEST, e.getMessage());

        } catch (Exception e) {

            System.out.println(e.getMessage());
            return List.of(HttpStatus.BAD_REQUEST, "Ocorreu algum erro. Tente novamente mais tarde");
        }
    }

    @PutMapping("/admin/usuarios/setar")
    public List<?> SetAdmin(@RequestBody String usuario) throws Exception, UsuarioException {

        try {

            // Verifica se existem apenas números na string
            usuario = usuario.replaceAll("^\"|\"$", "");
            if (!usuario.matches("\\d+"))
                throw new UsuarioException("O código é inválido");

            // Converte para Long
            Long idLong = Long.parseLong(usuario);

            // Busca o usuário ou lança uma exceção caso não encontre
            Usuario user = usuarioRepository.findById(idLong).stream()
                    .findFirst()
                    .orElseThrow(() -> new UsuarioException("Usuário não encontrado"));

            // Se for admin retira o admin e se for cliente, seta o admin
            adminUsuariosService.AdminOuNao(user);

            return List.of(HttpStatus.OK);

        } catch (UsuarioException e) {

            System.out.println(e.getMessage());
            return List.of(HttpStatus.BAD_REQUEST, e.getMessage());

        } catch (Exception e) {

            System.out.println(e.getMessage());
            return List.of(HttpStatus.BAD_REQUEST, "Ocorreu algum erro. Tente novamente mais tarde");
        }
    }
}
