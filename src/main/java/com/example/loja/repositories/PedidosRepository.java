package com.example.loja.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.loja.models.Pedidos;

public interface PedidosRepository extends JpaRepository<Pedidos, Long>{
    
    @Query("SELECT p FROM Pedidos p WHERE p.email = :email")
    List<Pedidos> findByEmail(@Param("email") String email);
}
