package model;

import enums.PerfilParticipante;
import enums.TipoUsuario;

import java.util.Objects;

public class Usuario {
    private int id;
    private String nome;
    private String email;
    private String senha;
    private TipoUsuario tipo;
    private PerfilParticipante perfil;

    public Usuario() {}

    public Usuario(int id, String nome, String email, String senha, TipoUsuario tipo, PerfilParticipante perfil) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.tipo = tipo;
        this.perfil = perfil;
    }

    public Usuario(String nome, String email, String senha, TipoUsuario tipo, PerfilParticipante perfil) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.tipo = tipo;
        this.perfil = perfil;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public TipoUsuario getTipo() { return tipo; }
    public void setTipo(TipoUsuario tipo) { this.tipo = tipo; }
    public PerfilParticipante getPerfil() { return perfil; }
    public void setPerfil(PerfilParticipante perfil) { this.perfil = perfil; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return id == usuario.id && Objects.equals(email, usuario.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}