package com.example.consumidor_notificacao.service;

import com.example.consumidor_notificacao.config.RabbitConfig;
import com.example.consumidor_notificacao.dto.PedidoIngressoDTO;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class DLQConsumer {

    private final RabbitTemplate rabbitTemplate;

    private static final int MAX_RETRY = 3;

    public DLQConsumer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = RabbitConfig.DLQ)
    public void processarDLQ(PedidoIngressoDTO pedido, Message message) {

        Integer retryCount = (Integer) message.getMessageProperties()
                .getHeaders()
                .getOrDefault("x-retry-count", 0);

        System.out.println("💀 DLQ recebida: " + pedido.getNomeCliente());
        System.out.println("🔁 Tentativa atual: " + retryCount);

        if (retryCount >= MAX_RETRY) {
            System.out.println("🚫 Limite de tentativas atingido. Descartando mensagem.");
            return;
        }

        try {
            System.out.println("🔁 Reenviando para fila principal...");

            rabbitTemplate.convertAndSend(
                    RabbitConfig.EXCHANGE,
                    RabbitConfig.ROUTING_KEY,
                    pedido,
                    msg -> {
                        msg.getMessageProperties().getHeaders()
                                .put("x-retry-count", retryCount + 1);
                        return msg;
                    }
            );

            System.out.println("✅ Reprocessamento enviado!");

        } catch (Exception e) {
            System.out.println("❌ Falha ao reenviar para fila");
        }
    }
}