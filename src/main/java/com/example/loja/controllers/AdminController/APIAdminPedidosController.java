package com.example.loja.controllers.AdminController;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.loja.enums.Pedido;
import com.example.loja.exceptions.PedidosException;
import com.example.loja.models.Pedidos;
import com.example.loja.repositories.PedidosRepository;

@RestController
public class APIAdminPedidosController {

    private final PedidosRepository pedidosRepository;

    public APIAdminPedidosController(PedidosRepository pedidosRepository){
        this.pedidosRepository = pedidosRepository;
    }

    @GetMapping("/admin/get/pedidos")
    public List<Pedidos> AdminAPIPedidosGET(@PathVariable("id") String id) {

        return pedidosRepository.findAll();

    }

    @GetMapping("/admin/pedidos/edit/status/{id}")
    public ResponseEntity<?> UpdatePedido(@PathVariable("id") String id, Pedido status) throws Exception, PedidosException{

        try {
            
            // Verifica se é um número
            if(!id.matches("\\d+")){
                throw new PedidosException("O id não é um número válido");
            }

            // Converte para Long
            Long idLong = Long.parseLong(id);

            // Verifica se existe no banco de dados
            if(!pedidosRepository.existsById(idLong)){
                throw new PedidosException("Este produto não existe");
            }


        } catch(PedidosException e) {

            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (Exception e) {

            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body("Ocorreu algum erro. Tente novamente mais tarde");
        }
    
        return ResponseEntity.ok(200);

    }
}