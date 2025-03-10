package com.example.loja.controllers.AdminController;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.loja.models.Pedidos;
import com.example.loja.repositories.PedidosRepository;

@RestController
public class APIAdminPedidosController {

    private final PedidosRepository pedidosRepository;

    public APIAdminPedidosController(PedidosRepository pedidosRepository) {
        this.pedidosRepository = pedidosRepository;
    }

    @GetMapping("/admin/pedidos/{id}")
    public List<?> AdminPedidosGET(@PathVariable("id") String id) {

        try {

            if (!id.matches("\\d+")) {
                return List.of("erro", "O código  é inválido");
            }

            Long idLong = Long.parseLong(id);

            Optional<Pedidos> produto = pedidosRepository.findById(idLong);

            return produto.map(Collections::singletonList)
                    .orElseGet(Collections::emptyList);

        } catch (Exception e) {

            return List.of("erro", e.getMessage());
        }
    }
}