package com.estudo.estoqueloja.service;

import com.estudo.estoqueloja.DTOs.ProdutoRequestDTO;
import com.estudo.estoqueloja.DTOs.ProdutoResponseDTO;
import com.estudo.estoqueloja.model.Produto;
import com.estudo.estoqueloja.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service // 9
public class ProdutoService {

    @Autowired // 10
    private ProdutoRepository repository;

    @Autowired
    private RabbitTemplate rabbitTemplate; // 11

    @Transactional // 12
    public ProdutoResponseDTO salvarProduto(ProdutoRequestDTO dto) {
        Produto produto = new Produto();
        produto.setNome(dto.nome());
        produto.setQuantidade(dto.quantidade());
        produto.setPreco(dto.preco());

        produto = repository.save(produto);

        // Enviando aviso de "Novo Produto" para a fila do RabbitMQ
        rabbitTemplate.convertAndSend("estoque.ex", "estoque.rk", "Novo produto: " + produto.getNome());

        return new ProdutoResponseDTO(
                produto.getId(),
                produto.getNome(),
                produto.getQuantidade(),
                produto.getPreco(),
                produto.calcularValorTotalEstoque() // Aqui você usa o método da Model
        );
    }
}