package com.estudo.estoqueloja.controller;

import com.estudo.estoqueloja.DTOs.ProdutoRequestDTO;
import com.estudo.estoqueloja.DTOs.ProdutoResponseDTO;
import com.estudo.estoqueloja.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @DeleteMapping("/{id}") // A URL será algo como: localhost:8080/produtos/1
    public ResponseEntity<Void> deletar(@PathVariable Long id) {

        service.deletar(id);

        // Retorna status 204 (No Content), padrão para deletar com sucesso
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/baixa")
    public ResponseEntity<ProdutoResponseDTO> baixarEstoque(@PathVariable Long id, @RequestBody Integer quantidade) {

        ProdutoResponseDTO response = service.darBaixaEstoque(id, quantidade);

        return ResponseEntity.ok(response);
    }

    @GetMapping // A URL será apenas: localhost:8080/produtos
    public ResponseEntity<List<ProdutoResponseDTO>> listar() {

        List<ProdutoResponseDTO> lista = service.listarTodos();

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> buscarUm(@PathVariable Long id) {

        ProdutoResponseDTO dto = service.buscarPorId(id);

        return ResponseEntity.ok(dto);
    }
}