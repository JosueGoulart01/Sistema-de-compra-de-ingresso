package com.example.RabbitMQ.service;

import com.example.RabbitMQ.config.RabbitConfig;
import com.example.RabbitMQ.dto.PedidoIngressoDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMqService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // Producer
    public void comprarIngresso(PedidoIngressoDTO pedido) {
        rabbitTemplate.convertAndSend(
        RabbitConfig.EXCHANGE_VENDA,
        RabbitConfig.ROUTING_KEY,
        pedido
        );
        System.out.println("🟡 Pedido enviado: " + pedido.getNomeCliente());
    }
}
