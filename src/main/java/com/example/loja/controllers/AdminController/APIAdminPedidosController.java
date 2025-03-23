package com.example.loja.controllers.AdminController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class APIAdminPedidosController {

    @GetMapping("/admin/pedidos/{id}")
    public void AdminPedidosGET(@PathVariable("id") String id) {


    }
}