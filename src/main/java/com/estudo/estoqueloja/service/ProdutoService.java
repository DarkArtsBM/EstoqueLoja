package com.estudo.estoqueloja.service;

import com.estudo.estoqueloja.DTOs.ProdutoRequestDTO;
import com.estudo.estoqueloja.DTOs.ProdutoResponseDTO;
import com.estudo.estoqueloja.model.Produto;
import com.estudo.estoqueloja.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    // Deletar Estoque
    @Transactional
    public void deletar(Long id) {
        // 1. Verifica se o produto existe antes de tentar apagar
        if (repository.existsById(id)) {

            // 2. Manda o banco de dados apagar
            repository.deleteById(id);

            // 3. (Opcional) Avisa o sistema que houve uma remoção
            rabbitTemplate.convertAndSend("estoque.ex", "estoque.rk", "Produto deletado. ID: " + id);

        } else {
            // 4. Se não existir, lança um erro (o Controller vai receber isso)
            throw new RuntimeException("Produto não encontrado com o ID: " + id);
        }
    }

    //Atualizar Estoque

    @Transactional // Importante pois vamos alterar dados e mandar mensagem
    public ProdutoResponseDTO darBaixaEstoque(Long id, Integer quantidadeParaRetirar) {

        // 1. Procura o produto (lança erro se não achar)
        Produto produto = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado ID: " + id));

        // 2. Valida se tem estoque suficiente
        if (produto.getQuantidade() < quantidadeParaRetirar) {
            throw new RuntimeException("Estoque insuficiente! Tens apenas " + produto.getQuantidade());
        }

        // 3. Atualiza a quantidade (A Lógica de Negócio)
        int novaQuantidade = produto.getQuantidade() - quantidadeParaRetirar;
        produto.setQuantidade(novaQuantidade);

        // 4. Salva a alteração no banco
        repository.save(produto);

        // 5. Avisa o RabbitMQ sobre a venda
        rabbitTemplate.convertAndSend("estoque.ex", "estoque.rk",
                "Venda realizada: " + produto.getNome() + ". Restam: " + novaQuantidade);

        // 6. Retorna o produto atualizado
        return new ProdutoResponseDTO(
                produto.getId(),
                produto.getNome(),
                produto.getQuantidade(),
                produto.getPreco(),
                produto.calcularValorTotalEstoque()
        );
    }

    //Buscar todos os Produtos
    public List<ProdutoResponseDTO> listarTodos() {
        // 1. Busca todos os produtos do banco
        List<Produto> produtos = repository.findAll();

        // 2. Converte a lista de Entidades para lista de DTOs
        return produtos.stream()
                .map(produto -> new ProdutoResponseDTO(
                        produto.getId(),
                        produto.getNome(),
                        produto.getQuantidade(),
                        produto.getPreco(),
                        produto.calcularValorTotalEstoque()
                ))
                .toList(); // Cria a lista final de DTOs
    }

    public ProdutoResponseDTO buscarPorId(Long id) {

        // 1. Tenta achar no banco. Se não achar, lança erro imediatamente.
        Produto produto = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com o ID: " + id));

        // 2. Se achou, converte para o DTO de resposta (igual fizemos no salvar)
        return new ProdutoResponseDTO(
                produto.getId(),
                produto.getNome(),
                produto.getQuantidade(),
                produto.getPreco(),
                produto.calcularValorTotalEstoque()
        );
    }

}