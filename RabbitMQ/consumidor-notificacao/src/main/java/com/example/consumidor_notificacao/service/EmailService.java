package com.example.consumidor_notificacao.service;

import com.example.consumidor_notificacao.dto.PedidoIngressoDTO;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    // ✅ MÉTODO ANTIGO (mantém)
    public void enviarEmailHTML(String para, String nome, String evento, int quantidade) {
        try {
            Context context = new Context();
            context.setVariable("nome", nome);
            context.setVariable("evento", evento);
            context.setVariable("quantidade", quantidade);

            String html = templateEngine.process("email", context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(para);
            helper.setSubject("🎟️ Confirmação de Compra");
            helper.setText(html, true);

            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao enviar email", e);
        }
    }

    // 🚀 NOVO MÉTODO (com PDF)
    public void enviarEmailComPdf(String para, PedidoIngressoDTO pedido, byte[] pdfBytes) {

        try {
            Context context = new Context();
            context.setVariable("nome", pedido.getNomeCliente());
            context.setVariable("evento", pedido.getEvento());
            context.setVariable("quantidade", pedido.getQuantidade());

            String html = templateEngine.process("email", context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(para);
            helper.setSubject("🎟️ Seu ingresso");
            helper.setText(html, true);

            // 📎 ANEXO PDF
            helper.addAttachment(
                    "ingresso.pdf",
                    new ByteArrayResource(pdfBytes)
            );

            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao enviar email com PDF", e);
        }
    }
}