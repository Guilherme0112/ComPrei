package com.example.loja.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.loja.models.ResetPassword;

public interface ResetPasswordRepository  extends JpaRepository<ResetPassword, Long>{
    
    @Query("SELECT r FROM ResetPassword r WHERE r.token = :token")
    List<ResetPassword> findByToken(@Param("token") String token);
}
