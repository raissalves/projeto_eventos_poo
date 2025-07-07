package model;

import enums.TipoUsuario;
import enums.PerfilParticipante;

public class Participante extends Usuario {
    public Participante(String nome, String email, String senha, PerfilParticipante perfil) {
        super(nome, email, senha, TipoUsuario.PARTICIPANTE, perfil);
    }
}