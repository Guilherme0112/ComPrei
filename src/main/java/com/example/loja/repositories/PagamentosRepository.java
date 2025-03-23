package com.example.loja.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.loja.models.Pagamentos;

public interface PagamentosRepository extends JpaRepository<Pagamentos, Long>{
  
    @Query("SELECT p FROM Pagamentos p WHERE p.email = :email")
    List<Pagamentos> findByEmail(@Param("email") String email);

    @Query("SELECT p FROM Pagamentos p WHERE p.pagamento_id = :id")
    List<Pagamentos> findByPaymentId(@Param("id") String id);

    @Query("SELECT p FROM Pagamentos p WHERE p.preferencia_id = :id")
    List<Pagamentos> findByPreferenceId(@Param("id") String id);
}
