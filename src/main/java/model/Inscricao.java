package model;

import enums.StatusPagamento;

import java.time.LocalDateTime;

public class Inscricao {
    private int id;
    private int participanteId;
    private int eventoId;
    private int atividadeId; // 0 se for inscrição apenas no evento
    private LocalDateTime dataInscricao;
    private StatusPagamento statusPagamento;
    private double valorPago;

    // Construtores
    public Inscricao() {}

    public Inscricao(int participanteId, int eventoId, Integer atividadeId, double valorPago) {
        this.participanteId = participanteId;
        this.eventoId = eventoId;
        this.atividadeId = atividadeId != null ? atividadeId : 0;
        this.valorPago = valorPago;
        this.dataInscricao = LocalDateTime.now();
        this.statusPagamento = StatusPagamento.PENDENTE;
    }

    public Inscricao(int participanteId, int eventoId, int atividadeId, double valorPago) {
        this.participanteId = participanteId;
        this.eventoId = eventoId;
        this.atividadeId = atividadeId;
        this.valorPago = valorPago;
        this.dataInscricao = LocalDateTime.now();
        this.statusPagamento = StatusPagamento.PENDENTE;
    }

    public Inscricao(int participanteId, int eventoId, double valorPago) {
        this(participanteId, eventoId, 0, valorPago);
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getParticipanteId() { return participanteId; }
    public void setParticipanteId(int participanteId) { this.participanteId = participanteId; }
    public int getEventoId() { return eventoId; }
    public void setEventoId(int eventoId) { this.eventoId = eventoId; }
    public int getAtividadeId() { return atividadeId; }
    public void setAtividadeId(int atividadeId) { this.atividadeId = atividadeId; }
    public LocalDateTime getDataInscricao() { return dataInscricao; }
    public void setDataInscricao(LocalDateTime dataInscricao) { this.dataInscricao = dataInscricao; }
    public StatusPagamento getStatusPagamento() { return statusPagamento; }
    public void setStatusPagamento(StatusPagamento statusPagamento) { this.statusPagamento = statusPagamento; }
    public double getValorPago() { return valorPago; }
    public void setValorPago(double valorPago) { this.valorPago = valorPago; }
}