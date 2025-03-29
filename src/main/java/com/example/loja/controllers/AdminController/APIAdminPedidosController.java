package com.example.loja.controllers.AdminController;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.loja.exceptions.PedidosException;
import com.example.loja.models.Pedidos;
import com.example.loja.models.dto.UpdatePedidoDTO;
import com.example.loja.repositories.PedidosRepository;
import com.example.loja.service.AdminService.AdminPedidosService;
import com.example.loja.service.UsuarioService.AuthService;

import jakarta.servlet.http.HttpSession;

@RestController
public class APIAdminPedidosController {

    private final AdminPedidosService adminPedidosService;
    private final PedidosRepository pedidosRepository;
    private final AuthService authService;

    public APIAdminPedidosController(AdminPedidosService adminPedidosService,
                                     PedidosRepository pedidosRepository,
                                     AuthService authService){
        this.adminPedidosService = adminPedidosService;
        this.pedidosRepository = pedidosRepository;
        this.authService = authService;
    }

    @GetMapping("/admin/get/pedidos")
    public List<Pedidos> AdminAPIPedidosGET() {

        return pedidosRepository.findAll();

    }

    @PutMapping("/admin/pedidos/edit/status")
    public ResponseEntity<?> UpdatePedido(@RequestBody UpdatePedidoDTO updatePedidoDTO, HttpSession http) throws Exception, PedidosException{

        try {

            // Recebe os dados do DTO
            Long id = updatePedidoDTO.getId();
            String status = updatePedidoDTO.getStatus();
            String email = authService.getSession(http).getEmail();

            // // Atualiza o status do pedido
            adminPedidosService.updatePedido(id, status, email);
            
            return ResponseEntity.ok(200);

        } catch(PedidosException e) {

            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (Exception e) {

            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body("Ocorreu algum erro. Tente novamente mais tarde");
        }

    }
}