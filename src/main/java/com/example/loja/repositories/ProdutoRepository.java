package com.example.loja.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
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
    Long countByCodigo(@Param("codigo") String codigo);

    @Query(value = "SELECT * FROM produtos p WHERE p.id IN (SELECT MIN(p2.id) FROM produtos p2 GROUP BY p2.codigo) ORDER BY RAND()", nativeQuery = true)
    Page<Produto> findRandom(Pageable pageable);

    @Query(value = """
        SELECT * FROM produtos p
        WHERE p.id IN (
            SELECT MIN(p2.id)
            FROM produtos p2
            WHERE LOWER(p2.name) LIKE LOWER(CONCAT('%', :query, '%'))
            GROUP BY p2.codigo
        )
        ORDER BY RAND()
    """, nativeQuery = true)
    List<Produto> searchResults(@Param("query") String query);

    @Modifying
    @Transactional
    @Query("DELETE FROM Produto p WHERE p.codigo = :codigo")
    void deleteByCodigo(@Param("codigo") String codigo);
}
