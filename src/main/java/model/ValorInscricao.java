package model;

import enums.PerfilParticipante;

public class ValorInscricao {
    private int eventoId;
    private PerfilParticipante perfil;
    private double valor;

    // Construtores
    public ValorInscricao() {}

    public ValorInscricao(int eventoId, String perfil, double valor) {
        this.eventoId = eventoId;
        this.perfil = PerfilParticipante.valueOf(perfil);
        this.valor = valor;
    }

    public ValorInscricao(int eventoId, PerfilParticipante perfil, double valor) {
        this.eventoId = eventoId;
        this.perfil = perfil;
        this.valor = valor;
    }

    // Getters e Setters
    public int getEventoId() { return eventoId; }
    public void setEventoId(int eventoId) { this.eventoId = eventoId; }
    public PerfilParticipante getPerfil() { return perfil; }
    public void setPerfil(PerfilParticipante perfil) { this.perfil = perfil; }
    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }
}