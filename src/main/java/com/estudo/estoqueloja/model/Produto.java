package com.estudo.estoqueloja.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity // 1
@Table(name = "tb_produtos") // 2
@Data // 3
public class Produto {
    @Id // 4
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 5
    private Long id;

    private String nome;
    private Integer quantidade;
    private Double preco;

    public Double calcularValorTotalEstoque() {
        if (this.preco == null || this.quantidade == null) {
            return 0.0;
        }
        return this.preco * this.quantidade;
    }
    // att
}