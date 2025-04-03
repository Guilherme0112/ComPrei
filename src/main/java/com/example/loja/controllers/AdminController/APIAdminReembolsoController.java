package com.example.loja.controllers.AdminController;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.loja.enums.Reembolso;
import com.example.loja.exceptions.PedidosException;
import com.example.loja.exceptions.UsuarioException;
import com.example.loja.models.Reembolsos;
import com.example.loja.models.Usuario;
import com.example.loja.models.dto.UpdateStatusDTO;
import com.example.loja.models.dto.UpdateReembolsoDTO;
import com.example.loja.repositories.ReembolsosRepository;
import com.example.loja.repositories.UsuarioRepository;
import com.example.loja.service.ReembolsoService;
import com.example.loja.service.UsuarioService.AuthService;

@RestController        
public class APIAdminReembolsoController {
    
    private final AuthService authService;
    private final ReembolsoService reembolsoService;
    private final ReembolsosRepository reembolsoRepository;
    private final UsuarioRepository usuarioRepository;

    public APIAdminReembolsoController(AuthService authService,
                                       ReembolsoService reembolsoService,
                                       ReembolsosRepository reembolsoRepository,
                                       UsuarioRepository usuarioRepository) {
        this.reembolsoService = reembolsoService;
        this.authService = authService;
        this.reembolsoRepository = reembolsoRepository;
        this.usuarioRepository = usuarioRepository;
    }

     @GetMapping("/admin/reembolso/{status}")
    public List<?> AdminAPIPedidosGET(@PathVariable("status") String status)
            throws Exception, PedidosException, UsuarioException {

        try {

            Reembolso reembolsoEnum = Reembolso.valueOf(status.toUpperCase());
            List<Reembolsos> reembolsoObj = reembolsoRepository.findByStatus(reembolsoEnum);

            reembolsoObj.stream()
                        .findFirst()
                        .orElseThrow(() -> new PedidosException("Reembolso nao encontrado"));

            List<Usuario> usuario = usuarioRepository.findByEmail(reembolsoObj.get(0).getEmail(), true);

            usuario.stream()
                   .findFirst()
                   .orElseThrow(() -> new UsuarioException("Usuario nao encontrado"));

            UpdateReembolsoDTO updateReembolsoDTO = new UpdateReembolsoDTO(
                    reembolsoObj.get(0).getId(),
                    reembolsoObj.get(0).getIdPedido(),
                    usuario.get(0).getName(),
                    usuario.get(0).getTelefone(),
                    reembolsoObj.get(0).getQuando()
            );

            return List.of(updateReembolsoDTO);

        } catch (PedidosException | UsuarioException e) {

            return List.of("erro", e.getMessage());

        } catch (Exception e) {

            System.out.println(e.getMessage());
            return List.of("erro", "Ocorreu algum erro. Tente novamente mais tarde");

        }
    }

    @PostMapping("/profile/reembolso/pedir")
    public ResponseEntity<?> reembolsoAPI(@RequestBody String id) throws Exception, PedidosException {

        try {

            // Pega o email da sessão e tenta criar o reeembolso
            String email = authService.buscarSessaUsuario().getEmail();
            reembolsoService.createReembolso(id, email);

            return ResponseEntity.ok().body(HttpStatus.ACCEPTED);

        } catch (PedidosException e) {

            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (Exception e) {

            System.out.println(e.getMessage());

            return ResponseEntity.badRequest().body("Ocorreu algum erro. Tente novamente mais tarde");
        }
    }

    @PutMapping("/profile/reembolso/editar")
    public ResponseEntity<?> updateReembolsoAPI(@RequestBody UpdateStatusDTO updateReembolso) throws Exception, PedidosException {

        try {

            // Pega os valroes
            String status = updateReembolso.getStatus();
            Long id = updateReembolso.getId();

            // Busca o reembolso no banco de dados
            Reembolsos reembolso = reembolsoRepository.findById(id)
                                                      .orElseThrow(() -> new PedidosException("Reembolso nao encontrado"));

            // Seta o novo status
            reembolso.setStatus(Reembolso.valueOf(status.toUpperCase()));

            // Atualiza o reembolso
            reembolsoRepository.save(reembolso);

            return ResponseEntity.ok(HttpStatus.OK);

        } catch (PedidosException e) {

            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (Exception e) {

            System.out.println(e.getMessage());

            return ResponseEntity.badRequest().body("Ocorreu algum erro. Tente novamente mais tarde");
        }
    }
}
