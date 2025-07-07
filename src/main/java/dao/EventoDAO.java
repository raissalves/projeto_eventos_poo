package dao;

import config.DatabaseConfig;
import model.Evento;
import exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventoDAO {

    public void inserir(Evento evento) {
        String sql = "INSERT INTO evento (titulo, descricao, data_inicio, data_fim, localizacao, administrador_id) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DatabaseConfig.getConnection();
            ps = conn.prepareStatement(sql);

            ps.setString(1, evento.getTitulo());
            ps.setString(2, evento.getDescricao());
            ps.setTimestamp(3, Timestamp.valueOf(evento.getDataInicio()));
            ps.setTimestamp(4, Timestamp.valueOf(evento.getDataFim()));
            ps.setString(5, evento.getLocalizacao());
            ps.setInt(6, evento.getAdministradorId());

            ps.executeUpdate();

            // Para SQLite, usar uma consulta separada para obter o último ID inserido
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid()")) {
                if (rs.next()) {
                    evento.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao inserir evento: " + e.getMessage());
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                // Ignorar erros ao fechar recursos
            }
        }
    }

    public Evento buscarPorId(int id) {
        String sql = "SELECT * FROM evento WHERE id = ?";
        Evento evento = null;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    evento = new Evento();
                    evento.setId(rs.getInt("id"));
                    evento.setTitulo(rs.getString("titulo"));
                    evento.setDescricao(rs.getString("descricao"));
                    evento.setDataInicio(rs.getTimestamp("data_inicio").toLocalDateTime());
                    evento.setDataFim(rs.getTimestamp("data_fim").toLocalDateTime());
                    evento.setLocalizacao(rs.getString("localizacao"));
                    evento.setAdministradorId(rs.getInt("administrador_id"));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar evento por ID: " + e.getMessage());
        }
        return evento;
    }

    public List<Evento> listarTodos() {
        String sql = "SELECT * FROM evento";
        List<Evento> eventos = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Evento evento = new Evento();
                evento.setId(rs.getInt("id"));
                evento.setTitulo(rs.getString("titulo"));
                evento.setDescricao(rs.getString("descricao"));
                evento.setDataInicio(rs.getTimestamp("data_inicio").toLocalDateTime());
                evento.setDataFim(rs.getTimestamp("data_fim").toLocalDateTime());
                evento.setLocalizacao(rs.getString("localizacao"));
                evento.setAdministradorId(rs.getInt("administrador_id"));
                eventos.add(evento);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao listar eventos: " + e.getMessage());
        }
        return eventos;
    }

    public void atualizar(Evento evento) {
        String sql = "UPDATE evento SET titulo = ?, descricao = ?, data_inicio = ?, data_fim = ?, localizacao = ?, administrador_id = ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, evento.getTitulo());
            ps.setString(2, evento.getDescricao());
            ps.setTimestamp(3, Timestamp.valueOf(evento.getDataInicio()));
            ps.setTimestamp(4, Timestamp.valueOf(evento.getDataFim()));
            ps.setString(5, evento.getLocalizacao());
            ps.setInt(6, evento.getAdministradorId());
            ps.setInt(7, evento.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao atualizar evento: " + e.getMessage());
        }
    }

    public void excluir(int id) {
        String sql = "DELETE FROM evento WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao excluir evento: " + e.getMessage());
        }
    }
}