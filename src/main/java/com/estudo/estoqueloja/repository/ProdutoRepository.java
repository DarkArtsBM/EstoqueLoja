package com.estudo.estoqueloja.repository;

import com.estudo.estoqueloja.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // 7
public interface ProdutoRepository extends JpaRepository<Produto, Long> { } // 8