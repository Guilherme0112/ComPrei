package com.example.loja.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.loja.models.Pedidos;

public interface PedidosRepository extends JpaRepository<Pedidos, Long>{
    
    
}
