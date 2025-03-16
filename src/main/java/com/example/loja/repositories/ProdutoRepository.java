package com.example.loja.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.loja.models.Produto;

import jakarta.transaction.Transactional;

public interface ProdutoRepository extends JpaRepository<Produto, Long>{
 
    @Query("SELECT p FROM Produto p WHERE p.codigo = :codigo")
    List<Produto> findByCodigoDeBarras(@Param("codigo") String codigo, Pageable pageable);

    @Query("SELECT COUNT(p) FROM Produto p WHERE p.codigo = :codigo")
    Long coutByCodigo(@Param("codigo") String codigo);

    @Modifying
    @Transactional
    @Query("DELETE FROM Produto p WHERE p.codigo = :codigo")
    void deleteByCodigo(@Param("codigo") String codigo);
}
