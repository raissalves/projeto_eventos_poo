package model;

import enums.TipoUsuario;

public class Administrador extends Usuario {
    public Administrador(String nome, String email, String senha) {
        super(nome, email, senha, TipoUsuario.ADMIN, null);
    }
}