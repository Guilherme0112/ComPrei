package com.example.loja.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.loja.models.UsuarioAddress;

public interface UsuarioAddressRepository extends JpaRepository<UsuarioAddress, Long>{
    
    @Query("SELECT u FROM UsuarioAddress u WHERE u.user_email = :email")
    List<UsuarioAddress> findByEmail(@Param("email") String email);
}
