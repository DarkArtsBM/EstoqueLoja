package com.estudo.estoqueloja.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Nomes que você já está usando no ProdutoService
    public static final String EXCHANGE_NAME = "estoque.ex";
    public static final String QUEUE_NAME = "estoque.fila.vendas";
    public static final String ROUTING_KEY = "estoque.rk";

    @Bean
    public Queue queue() {
        // Cria a fila se ela não existir
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public DirectExchange exchange() {
        // Cria a Exchange do tipo Direct
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        // Faz o "link" entre a fila e a exchange usando a Routing Key
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }
}