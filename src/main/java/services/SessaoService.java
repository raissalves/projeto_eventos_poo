package services;

import model.Usuario;
import enums.TipoUsuario;

public class SessaoService {
    private static Usuario usuarioLogado;

    public static void iniciarSessao(Usuario usuario) {
        usuarioLogado = usuario;
    }

    public static void encerrarSessao() {
        usuarioLogado = null;
    }

    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public static boolean isAdministrador() {
        return usuarioLogado != null &&
                usuarioLogado.getTipo() == TipoUsuario.ADMIN;
    }

    public static boolean isParticipante() {
        return usuarioLogado != null &&
                usuarioLogado.getTipo() == TipoUsuario.PARTICIPANTE;
    }
}