package model;

import enums.TipoAtividade;
import java.time.LocalDateTime;

public class Atividade {
    private int id;
    private String nome;
    private String descricao;
    private LocalDateTime dataRealizacao;
    private int limiteInscritos;
    private TipoAtividade tipo;
    private int eventoId;

    // Construtores
    public Atividade() {}

    public Atividade(String nome, String descricao, LocalDateTime dataRealizacao, 
                    int limiteInscritos, TipoAtividade tipo, int eventoId) {
        this.nome = nome;
        this.descricao = descricao;
        this.dataRealizacao = dataRealizacao;
        this.limiteInscritos = limiteInscritos;
        this.tipo = tipo;
        this.eventoId = eventoId;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public LocalDateTime getDataRealizacao() { return dataRealizacao; }
    public void setDataRealizacao(LocalDateTime dataRealizacao) { this.dataRealizacao = dataRealizacao; }
    public int getLimiteInscritos() { return limiteInscritos; }
    public void setLimiteInscritos(int limiteInscritos) { this.limiteInscritos = limiteInscritos; }
    public TipoAtividade getTipo() { return tipo; }
    public void setTipo(TipoAtividade tipo) { this.tipo = tipo; }
    public int getEventoId() { return eventoId; }
    public void setEventoId(int eventoId) { this.eventoId = eventoId; }
}