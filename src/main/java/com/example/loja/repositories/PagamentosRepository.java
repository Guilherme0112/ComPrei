package com.example.loja.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.loja.models.Pagamentos;

public interface PagamentosRepository extends JpaRepository<Pagamentos, Long>{
  
  
}
