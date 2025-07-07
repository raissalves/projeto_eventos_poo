package dao;

import config.DatabaseConfig;
import model.Usuario;
import enums.TipoUsuario;
import enums.PerfilParticipante;

import exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public void inserir(final Usuario usuario) {
        // Verificar se usuário já existe
        if (usuarioExiste(usuario.getEmail())) {
            throw new DatabaseException("Usuário com email " + usuario.getEmail() + " já existe");
        }

        if (usuario.getTipo() == TipoUsuario.PARTICIPANTE && usuario.getPerfil() == null) {
            throw new IllegalArgumentException("Participantes devem ter um perfil definido.");
        }

        String sql = "INSERT INTO usuario (nome, email, senha, tipo, perfil) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DatabaseConfig.getConnection();
            ps = conn.prepareStatement(sql);

            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getSenha());
            ps.setString(4, usuario.getTipo().toString());
            ps.setString(5, usuario.getPerfil() != null ? usuario.getPerfil().toString() : null);

            ps.executeUpdate();

            // Para SQLite, usar uma consulta separada para obter o último ID inserido
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid()")) {
                if (rs.next()) {
                    usuario.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao inserir usuário: " + e.getMessage());
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                // Ignorar erros ao fechar recursos
            }
        }
    }

    public boolean usuarioExiste(String email) {
        String sql = "SELECT COUNT(*) FROM usuario WHERE email = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao verificar existência do usuário: " + e.getMessage());
        }
    }

    public int obterProximoId() {
        String sql = "SELECT COALESCE(MAX(id), 0) + 1 FROM usuario";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 1; // Se não há usuários, começar com ID 1
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao obter próximo ID: " + e.getMessage());
        }
    }

    public Usuario buscarPorEmail(final String email) {
        String sql = "SELECT * FROM usuario WHERE email = ?";
        Usuario usuario = null;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("email"),
                            rs.getString("senha"),
                            TipoUsuario.valueOf(rs.getString("tipo")),
                            rs.getString("perfil") != null ?
                                    PerfilParticipante.valueOf(rs.getString("perfil")) : null
                    );
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar usuário por email: " + e.getMessage());
        }
        return usuario;
    }

    public Usuario buscarPorId(int id) {
        String sql = "SELECT * FROM usuario WHERE id = ?";
        Usuario usuario = null;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("email"),
                            rs.getString("senha"),
                            TipoUsuario.valueOf(rs.getString("tipo")),
                            rs.getString("perfil") != null ?
                                    PerfilParticipante.valueOf(rs.getString("perfil")) : null
                    );
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar usuário por ID: " + e.getMessage());
        }
        return usuario;
    }

    public List<Usuario> listarTodos() {
        String sql = "SELECT * FROM usuario";
        List<Usuario> usuarios = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("senha"),
                        TipoUsuario.valueOf(rs.getString("tipo")),
                        rs.getString("perfil") != null ?
                                PerfilParticipante.valueOf(rs.getString("perfil")) : null
                );
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao listar usuários: " + e.getMessage());
        }
        return usuarios;
    }

    public void atualizar(Usuario usuario) {
        String sql = "UPDATE usuario SET nome = ?, email = ?, senha = ?, tipo = ?, perfil = ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getSenha());
            ps.setString(4, usuario.getTipo().toString());
            ps.setString(5, usuario.getPerfil() != null ? usuario.getPerfil().toString() : null);
            ps.setInt(6, usuario.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao atualizar usuário: " + e.getMessage());
        }
    }

    public void excluir(int id) {
        String sql = "DELETE FROM usuario WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao excluir usuário: " + e.getMessage());
        }
    }
}