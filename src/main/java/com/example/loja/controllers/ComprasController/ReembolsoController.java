package com.example.loja.controllers.ComprasController;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.loja.enums.Reembolso;
import com.example.loja.exceptions.PedidosException;
import com.example.loja.exceptions.SessionException;
import com.example.loja.models.Reembolsos;
import com.example.loja.models.dto.UpdatePedidoDTO;
import com.example.loja.repositories.ReembolsosRepository;
import com.example.loja.service.ReembolsoService;
import com.example.loja.service.UsuarioService.AuthService;

import jakarta.servlet.http.HttpSession;

@RestController
public class ReembolsoController {

    private final ReembolsoService reembolsoService;
    private final AuthService authService;
    private final ReembolsosRepository reembolsoRepository;

    public ReembolsoController(ReembolsoService reembolsoService,
            AuthService authService,
            ReembolsosRepository reembolsoRepository) {
        this.reembolsoService = reembolsoService;
        this.authService = authService;
        this.reembolsoRepository = reembolsoRepository;
    }

    @GetMapping("/profile/reembolsos")
    public ModelAndView reembolso(HttpSession http) throws SessionException, Exception {

        ModelAndView mv = new ModelAndView();

        try {


            String email = authService.getSession(http).getEmail();

            mv.setViewName("views/produto/pedido/reembolso");
            mv.addObject("reembolsos", reembolsoRepository.findByEmail(email));

        } catch (Exception e) {

            mv.setViewName("redirect:/profile");
        }

        return mv;
    }

    @PostMapping("/profile/reembolso/pedir")
    public ResponseEntity<?> reembolsoAPI(@RequestBody String id, HttpSession http) throws Exception, PedidosException {

        try {

            // Pega o email da sessão e tenta criar o reeembolso
            String email = authService.getSession(http).getEmail();
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
    public ResponseEntity<?> updateReembolsoAPI(UpdatePedidoDTO updateReembolso) throws Exception, PedidosException {

        try {

            // Pega os valroes
            String status = updateReembolso.getStatus();
            Long id = updateReembolso.getId();

            // Busca o reembolso no banco de dados
            Optional<Reembolsos> reembolso = reembolsoRepository.findById(id);

            // Verifica se existe
            reembolso.stream()
            .findFirst()
            .orElseThrow(() -> new PedidosException("Reembolso nao encontrado"));

            // Seta o novo status
            reembolso.get().setStatus(Reembolso.valueOf(status.toUpperCase()));

            // Atualiza o reembolso
            reembolsoRepository.save(reembolso.get());

            return ResponseEntity.ok().body(HttpStatus.ACCEPTED);

        } catch (PedidosException e) {

            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (Exception e) {

            System.out.println(e.getMessage());

            return ResponseEntity.badRequest().body("Ocorreu algum erro. Tente novamente mais tarde");
        }
    }
}
