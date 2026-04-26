package com.example.consumidor_notificacao.service;

import com.example.consumidor_notificacao.dto.PedidoIngressoDTO;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.*;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.util.UUID;
import com.itextpdf.layout.properties.HorizontalAlignment; 
import java.awt.image.BufferedImage;                     
import javax.imageio.ImageIO;

@Service
public class PdfService {

    public byte[] gerarPdf(PedidoIngressoDTO pedido) {

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // 🆔 ID único
            String idIngresso = UUID.randomUUID().toString();

            // 🎨 TÍTULO
            Paragraph titulo = new Paragraph("🎟️ INGRESSO")
                    .setBold()
                    .setFontSize(20)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(ColorConstants.WHITE)
                    .setBackgroundColor(ColorConstants.BLACK)
                    .setPadding(10);

            document.add(titulo);

            document.add(new Paragraph("\n"));

            // 📦 BOX COM INFO
            Table table = new Table(2).useAllAvailableWidth();

            table.addCell("Cliente:");
            table.addCell(pedido.getNomeCliente());

            table.addCell("Evento:");
            table.addCell(pedido.getEvento());

            table.addCell("Quantidade:");
            table.addCell(String.valueOf(pedido.getQuantidade()));

            table.addCell("ID do Ingresso:");
            table.addCell(idIngresso);

            document.add(table);

            document.add(new Paragraph("\n"));

            // 📱 QR CODE (com ID dentro)
            byte[] qrCode = gerarQrCode(idIngresso);

            Image qrImage = new Image(ImageDataFactory.create(qrCode))
                    .setWidth(150)
                    .setHeight(150)
                    .setHorizontalAlignment(HorizontalAlignment.CENTER);

            document.add(qrImage);

            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Apresente este QR Code na entrada.")
                    .setTextAlignment(TextAlignment.CENTER));

            document.close();

            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF", e);
        }
    }

    // 🔳 Geração do QR Code
    private byte[] gerarQrCode(String conteudo) {
    try {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(conteudo, BarcodeFormat.QR_CODE, 200, 200);

        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < 200; x++) {
            for (int y = 0; y < 200; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? 0x000000 : 0xFFFFFF);
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", baos);

        return baos.toByteArray();

    } catch (Exception e) {
        throw new RuntimeException("Erro QR Code", e);
    }
    }
}