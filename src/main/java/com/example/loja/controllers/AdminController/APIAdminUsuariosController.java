package com.example.loja.controllers.AdminController;

import java.util.Collections;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/admin/usuarios/{id}")
    public List<?> AdminUsuariosAPIGET(@PathVariable("id") String id) throws Exception, UsuarioException{
        try {

            // Verifica se existem apenas números na string
            if (!id.matches("\\d+")) throw new UsuarioException("O código  é inválido");

            // Converte o id para Long
            Long idLong = Long.parseLong(id);

            // Pega o usuário como Optional e converte para List
            return usuarioRepository.findById(idLong).map(Collections::singletonList)
                                                     .orElseGet(Collections::emptyList);

        } catch (UsuarioException e) {
            
            return List.of("erro", e.getMessage());
        
        } catch (Exception e) {

            return List.of("erro", e.getMessage());
        }
    }

    @GetMapping("/admin/usuarios/banir/{id}")
    public List<?> BanirOrDesbanir(@PathVariable("id") String id) throws Exception, UsuarioException {

        try {

            // Verifica se existem apenas números na string
            if (!id.matches("\\d+")) {
                return List.of("erro", "O código  é inválido");
            }

            //  Busca o usuário ou lança uma exceção caso não encontre
            Usuario user = usuarioRepository.findById(Long.parseLong(id)).stream()
                                                                       .findFirst()
                                                                       .orElseThrow(() -> new UsuarioException("Usuário nao encontrado"));

            // Chama o método que bane se tiver desbanido e desbane se tiver banido
            adminUsuariosService.BanirOuDesbanir(user);

            return List.of(HttpStatus.OK);

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

            // Verifica se existem apenas números na string
            if (!id.matches("\\d+")) {
                return List.of("erro", "O código  é inválido");
            }

            // Converte para Long
            Long idLong = Long.parseLong(id);
            
            // Busca o usuário ou lança uma exceção caso não encontre
            Usuario user = usuarioRepository.findById(idLong).stream()
                                                             .findFirst()
                                                             .orElseThrow(() -> new UsuarioException("Usuário nao encontrado"));

            // Se for admin retira o admin e se for cliente, seta o admin
            adminUsuariosService.AdminOuNao(user);

            return List.of(HttpStatus.OK);

        } catch (UsuarioException e) {

            System.out.println(e.getMessage());
            return List.of("erro", e.getMessage());

        } catch (Exception e) {

            System.out.println(e.getMessage());
            return List.of("erro", "Ocorreu algum erro. Tente novamente mais tarde");
        }
    }
}
