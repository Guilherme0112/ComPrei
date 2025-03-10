package com.example.loja.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.loja.models.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long>{
 
    @Query("SELECT p FROM Produto p WHERE p.codigo = :codigo")
    List<Produto> findByCodigoDeBarras(@Param("codigo") String codigo);
}
