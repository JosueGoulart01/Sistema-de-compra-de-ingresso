package com.example.RabbitMQ.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;

import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE_VENDA = "exchange.venda";
    public static final String FILA_VENDA = "fila.venda.ingresso";
    public static final String ROUTING_KEY = "venda.ingresso";

    @Bean
    public Queue filaVenda() {
        return new Queue(FILA_VENDA, true);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_VENDA);
    }

    @Bean
    public Binding binding(Queue fila, DirectExchange exchange) {
        return BindingBuilder
                .bind(fila)
                .to(exchange)
                .with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}