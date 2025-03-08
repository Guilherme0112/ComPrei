package com.example.loja.repositories;

import com.example.loja.models.VerificationEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VerificationEmailRepository extends JpaRepository<VerificationEmail, Long> {

    @Query("SELECT v FROM VerificationEmail v WHERE v.token = :token")
    List<VerificationEmail> findByToken(@Param("token") String token);
        }
