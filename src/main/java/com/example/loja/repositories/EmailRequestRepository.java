package com.example.loja.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.loja.models.EmailRequests;

public interface EmailRequestRepository extends JpaRepository<EmailRequests, Long>{
    
    @Query("SELECT e FROM EmailRequests e WHERE e.email = :email")
    List<EmailRequests> findByEmail(@Param("email") String email);
}
