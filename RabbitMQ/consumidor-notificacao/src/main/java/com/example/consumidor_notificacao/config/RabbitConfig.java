package com.example.consumidor_notificacao.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.*;
import java.util.HashMap;
import java.util.Map;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE = "exchange.venda";
    public static final String FILA = "fila.venda.ingresso";
    
    // ATENÇÃO: Mudou de "venda" para "venda.ingresso" para bater com o Produtor
    public static final String ROUTING_KEY = "venda.ingresso"; 

    public static final String RETRY_FILA = "fila.venda.retry";
    public static final String RETRY_ROUTING_KEY = "venda.retry";

    public static final String DLQ = "fila.venda.dlq";
    public static final String DLQ_ROUTING_KEY = "venda.dlq";

   

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Queue filaPrincipal() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", EXCHANGE);
        args.put("x-dead-letter-routing-key", RETRY_ROUTING_KEY);
        return new Queue(FILA, true, false, false, args);
    }

    @Bean
    public Queue filaRetry() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-message-ttl", 5000);
        // E aqui
        args.put("x-dead-letter-exchange", EXCHANGE);
        args.put("x-dead-letter-routing-key", ROUTING_KEY);

        return new Queue(RETRY_FILA, true, false, false, args);
    }

    @Bean
    public Queue filaDLQ() {
        return new Queue(DLQ, true);
    }

    @Bean
    public Binding bindingPrincipal() {
        return BindingBuilder.bind(filaPrincipal())
                .to(exchange())
                .with(ROUTING_KEY);
    }

    @Bean
    public Binding bindingRetry() {
        return BindingBuilder.bind(filaRetry())
                .to(exchange())
                .with(RETRY_ROUTING_KEY);
    }

    @Bean
    public Binding bindingDLQ() {
        return BindingBuilder.bind(filaDLQ())
                .to(exchange())
                .with(DLQ_ROUTING_KEY);
    }
     @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}