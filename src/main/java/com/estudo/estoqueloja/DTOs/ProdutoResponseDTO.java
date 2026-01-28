package com.estudo.estoqueloja.DTOs;

public record ProdutoResponseDTO(
        Long id,
        String nome,
        Integer quantidade,
        Double preco,
        Double valorTotalEstoque // Um campo que só existe na saída

) { }