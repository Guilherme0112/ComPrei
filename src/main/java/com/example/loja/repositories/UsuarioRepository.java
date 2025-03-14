package com.example.loja.repositories;

import java.util.List;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.loja.models.Usuario;


public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
    
    @Query("SELECT u FROM Usuario u WHERE u.email = :email AND u.active = :active")
    List<Usuario> findByEmail(@Param("email") String email,
                              @Param("active") Boolean active);

    @Modifying
    @Transactional
    @Query("UPDATE Usuario u SET u.active = :active WHERE u.email = :email")
    void changeActive(@Param("active") boolean active,
                      @Param("email") String email);
}
