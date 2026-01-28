package com.estudo.estoqueloja.controller;

import com.estudo.estoqueloja.DTOs.ProdutoRequestDTO;
import com.estudo.estoqueloja.DTOs.ProdutoResponseDTO;
import com.estudo.estoqueloja.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService service;

    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> criar(@RequestBody ProdutoRequestDTO dto) {
        // 1. Recebe o RequestDTO (dados de entrada)
        // 2. O Service processa e retorna o ResponseDTO (dados de saída)
        ProdutoResponseDTO response = service.salvarProduto(dto);

        return ResponseEntity.ok(response);
    }
}