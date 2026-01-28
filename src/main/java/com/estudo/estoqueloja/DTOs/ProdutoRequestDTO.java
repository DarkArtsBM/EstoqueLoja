package com.estudo.estoqueloja.DTOs;

public record ProdutoRequestDTO(
        String nome,
        Integer quantidade,
        Double preco
) { }