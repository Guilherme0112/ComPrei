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
import com.example.loja.exceptions.ReembolsoException;
import com.example.loja.exceptions.UsuarioException;
import com.example.loja.models.Reembolsos;
import com.example.loja.models.dto.UpdateStatusDTO;
import com.example.loja.repositories.ReembolsosRepository;
import com.example.loja.service.ReembolsoService;
import com.example.loja.service.AdminService.AdminReembolsoService;
import com.example.loja.service.UsuarioService.AuthService;

@RestController        
public class APIAdminReembolsoController {
    
    private final AuthService authService;
    private final ReembolsoService reembolsoService;
    private final ReembolsosRepository reembolsoRepository;
    private final AdminReembolsoService adminReembolsoService;

    public APIAdminReembolsoController(AuthService authService,
                                       ReembolsoService reembolsoService,
                                       ReembolsosRepository reembolsoRepository,
                                       AdminReembolsoService adminReembolsoService) {
        this.reembolsoService = reembolsoService;
        this.authService = authService;
        this.reembolsoRepository = reembolsoRepository;
        this.adminReembolsoService = adminReembolsoService;
    }

    @GetMapping("/admin/reembolso/{status}")
    public List<?> AdminAPIPedidosGET(@PathVariable("status") String status) throws Exception, ReembolsoException, UsuarioException {

        try {

            // Tenta converter o status (String) para o enum
            Reembolso reembolsoEnum = Reembolso.valueOf(status.toUpperCase());

            return adminReembolsoService.getReembolsoByStatus(reembolsoEnum);

        } catch (ReembolsoException | UsuarioException e) {

            return List.of("erro", e.getMessage());

        } catch (Exception e) {

            System.out.println(e.getMessage());
            return List.of("erro", "Ocorreu algum erro. Tente novamente mais tarde");

        }
    }

    @PostMapping("/admin/profile/reembolso/pedir")
    public ResponseEntity<?> reembolsoAPI(@RequestBody String id) throws Exception, PedidosException {

        try {

            // Pega o email da sessão e tenta criar o reeembolso
            String email = authService.buscarSessaoUsuario().getEmail();
            reembolsoService.createReembolso(id, email);

            return ResponseEntity.ok().body(HttpStatus.ACCEPTED);

        } catch (PedidosException e) {

            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (Exception e) {

            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body("Ocorreu algum erro. Tente novamente mais tarde");
        }
    }

    @PutMapping("/admin/profile/reembolso/editar")
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
