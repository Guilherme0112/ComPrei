package com.example.loja.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.loja.enums.Reembolso;
import com.example.loja.models.Reembolsos;

public interface ReembolsosRepository extends JpaRepository<Reembolsos, Long> {
    

    @Query("SELECT r FROM Reembolsos r WHERE r.idPedido = :id")
    List<Reembolsos> findByIdPedido(@Param("id") Long id);

    @Query("SELECT r FROM Reembolsos r WHERE r.email = :email")
    List<Reembolsos> findByEmail(@Param("email") String email);

    @Query("SELECT r FROM Reembolsos r WHERE r.status = :status")
    List<Reembolsos> findByStatus(@Param("status") Reembolso status);
}
