package services;

import dao.UsuarioDAO;
import model.Usuario;
import exceptions.AutenticacaoException;

public class AutenticacaoService {
    private final UsuarioDAO usuarioDAO;

    public AutenticacaoService(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public Usuario autenticar(String email, String senha) throws AutenticacaoException {
        Usuario usuario = usuarioDAO.buscarPorEmail(email);

        if (usuario == null) {
            throw new AutenticacaoException("Usuário não encontrado");
        }

        if (!usuario.getSenha().equals(senha)) {
            throw new AutenticacaoException("Senha incorreta");
        }

        return usuario;
    }
}