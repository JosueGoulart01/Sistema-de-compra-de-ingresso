package com.example.RabbitMQ.dto;

public class PedidoIngressoDTO {
    private String nomeCliente;
    private String email; 
    private String evento;
    private int quantidade;

    public PedidoIngressoDTO() {}

    // Adicione os Getters e Setters para o novo campo
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    // Mantenha os outros getters e setters...
    public String getNomeCliente() { return nomeCliente; }
    public void setNomeCliente(String nomeCliente) { this.nomeCliente = nomeCliente; }
    public String getEvento() { return evento; }
    public void setEvento(String evento) { this.evento = evento; }
    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
}