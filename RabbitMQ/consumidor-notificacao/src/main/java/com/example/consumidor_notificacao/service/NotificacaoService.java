package com.example.consumidor_notificacao.service;

import com.example.consumidor_notificacao.config.RabbitConfig;
import com.example.consumidor_notificacao.dto.PedidoIngressoDTO;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.stereotype.Service;

@Service
public class NotificacaoService {

    private final EmailService emailService;
    private final PdfService pdfService;

    public NotificacaoService(EmailService emailService, PdfService pdfService) {
        this.emailService = emailService;
        this.pdfService = pdfService;
    }

    @RabbitListener(queues = RabbitConfig.FILA)
    public void processarPedido(PedidoIngressoDTO pedido) {

        System.out.println("📩 Processando pedido...");

        byte[] pdf = pdfService.gerarPdf(pedido);

        emailService.enviarEmailComPdf(
                pedido.getEmail(),
                pedido,
                pdf
        );
    }
}