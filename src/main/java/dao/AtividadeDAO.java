package dao;

import config.DatabaseConfig;
import model.Atividade;
import enums.TipoAtividade;
import exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AtividadeDAO {

    public void inserir(Atividade atividade) {
        String sql = "INSERT INTO atividade (nome, descricao, data_realizacao, limite_inscritos, tipo, evento_id) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DatabaseConfig.getConnection();
            ps = conn.prepareStatement(sql);

            ps.setString(1, atividade.getNome());
            ps.setString(2, atividade.getDescricao());
            ps.setTimestamp(3, Timestamp.valueOf(atividade.getDataRealizacao()));
            ps.setInt(4, atividade.getLimiteInscritos());
            ps.setString(5, atividade.getTipo().toString());
            ps.setInt(6, atividade.getEventoId());

            ps.executeUpdate();

            // Para SQLite, usar uma consulta separada para obter o último ID inserido
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid()")) {
                if (rs.next()) {
                    atividade.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao inserir atividade: " + e.getMessage());
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                // Ignorar erros ao fechar recursos
            }
        }
    }

    public Atividade buscarPorId(int id) {
        String sql = "SELECT * FROM atividade WHERE id = ?";
        Atividade atividade = null;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    atividade = new Atividade();
                    atividade.setId(rs.getInt("id"));
                    atividade.setNome(rs.getString("nome"));
                    atividade.setDescricao(rs.getString("descricao"));
                    atividade.setDataRealizacao(rs.getTimestamp("data_realizacao").toLocalDateTime());
                    atividade.setLimiteInscritos(rs.getInt("limite_inscritos"));
                    atividade.setTipo(TipoAtividade.valueOf(rs.getString("tipo")));
                    atividade.setEventoId(rs.getInt("evento_id"));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar atividade por ID: " + e.getMessage());
        }
        return atividade;
    }

    public List<Atividade> listarPorEvento(int eventoId) {
        String sql = "SELECT * FROM atividade WHERE evento_id = ?";
        List<Atividade> atividades = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, eventoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Atividade atividade = new Atividade();
                    atividade.setId(rs.getInt("id"));
                    atividade.setNome(rs.getString("nome"));
                    atividade.setDescricao(rs.getString("descricao"));
                    atividade.setDataRealizacao(rs.getTimestamp("data_realizacao").toLocalDateTime());
                    atividade.setLimiteInscritos(rs.getInt("limite_inscritos"));
                    atividade.setTipo(TipoAtividade.valueOf(rs.getString("tipo")));
                    atividade.setEventoId(rs.getInt("evento_id"));
                    atividades.add(atividade);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao listar atividades do evento: " + e.getMessage());
        }
        return atividades;
    }

    public void atualizar(Atividade atividade) {
        String sql = "UPDATE atividade SET nome = ?, descricao = ?, data_realizacao = ?, limite_inscritos = ?, tipo = ?, evento_id = ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, atividade.getNome());
            ps.setString(2, atividade.getDescricao());
            ps.setTimestamp(3, Timestamp.valueOf(atividade.getDataRealizacao()));
            ps.setInt(4, atividade.getLimiteInscritos());
            ps.setString(5, atividade.getTipo().toString());
            ps.setInt(6, atividade.getEventoId());
            ps.setInt(7, atividade.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao atualizar atividade: " + e.getMessage());
        }
    }

    public void excluir(int id) {
        String sql = "DELETE FROM atividade WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao excluir atividade: " + e.getMessage());
        }
    }

    public List<Atividade> listarTodos() {
        String sql = "SELECT * FROM atividade";
        List<Atividade> atividades = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Atividade atividade = new Atividade();
                atividade.setId(rs.getInt("id"));
                atividade.setNome(rs.getString("nome"));
                atividade.setDescricao(rs.getString("descricao"));
                atividade.setDataRealizacao(rs.getTimestamp("data_realizacao").toLocalDateTime());
                atividade.setLimiteInscritos(rs.getInt("limite_inscritos"));
                atividade.setTipo(TipoAtividade.valueOf(rs.getString("tipo")));
                atividade.setEventoId(rs.getInt("evento_id"));
                atividades.add(atividade);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao listar atividades: " + e.getMessage());
        }
        return atividades;
    }
}