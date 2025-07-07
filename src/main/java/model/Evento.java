package model;

import java.time.LocalDateTime;

public class Evento {
    private int id;
    private String titulo;
    private String descricao;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private String localizacao;
    private int administradorId;

    // Construtores
    public Evento() {}

    public Evento(String titulo, String descricao, LocalDateTime dataInicio, 
                 LocalDateTime dataFim, String localizacao, int administradorId) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.localizacao = localizacao;
        this.administradorId = administradorId;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public LocalDateTime getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDateTime dataInicio) { this.dataInicio = dataInicio; }
    public LocalDateTime getDataFim() { return dataFim; }
    public void setDataFim(LocalDateTime dataFim) { this.dataFim = dataFim; }
    public String getLocalizacao() { return localizacao; }
    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }
    public int getAdministradorId() { return administradorId; }
    public void setAdministradorId(int administradorId) { this.administradorId = administradorId; }
}