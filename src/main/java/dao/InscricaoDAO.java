package dao;

import config.DatabaseConfig;
import model.Inscricao;
import enums.StatusPagamento;
import exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InscricaoDAO {

    public void inserir(Inscricao inscricao) {
        String sql = "INSERT INTO inscricao (participante_id, evento_id, atividade_id, data_inscricao, status_pagamento, valor_pago) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DatabaseConfig.getConnection();
            ps = conn.prepareStatement(sql);

            ps.setInt(1, inscricao.getParticipanteId());
            ps.setInt(2, inscricao.getEventoId());
            if (inscricao.getAtividadeId() == 0) {
                ps.setNull(3, Types.INTEGER);
            } else {
                ps.setInt(3, inscricao.getAtividadeId());
            }
            ps.setTimestamp(4, Timestamp.valueOf(inscricao.getDataInscricao()));
            ps.setString(5, inscricao.getStatusPagamento().toString());
            ps.setDouble(6, inscricao.getValorPago());

            ps.executeUpdate();

            // Para SQLite, usar uma consulta separada para obter o último ID inserido
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid()")) {
                if (rs.next()) {
                    inscricao.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao inserir inscrição: " + e.getMessage());
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                // Ignorar erros ao fechar recursos
            }
        }
    }

    public Inscricao buscarPorId(int id) {
        String sql = "SELECT * FROM inscricao WHERE id = ?";
        Inscricao inscricao = null;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    inscricao = new Inscricao();
                    inscricao.setId(rs.getInt("id"));
                    inscricao.setParticipanteId(rs.getInt("participante_id"));
                    inscricao.setEventoId(rs.getInt("evento_id"));
                    
                    // Tratamento correto para valores NULL
                    int atividadeId = rs.getInt("atividade_id");
                    inscricao.setAtividadeId(rs.wasNull() ? 0 : atividadeId);
                    
                    inscricao.setDataInscricao(rs.getTimestamp("data_inscricao").toLocalDateTime());
                    inscricao.setStatusPagamento(StatusPagamento.valueOf(rs.getString("status_pagamento")));
                    inscricao.setValorPago(rs.getDouble("valor_pago"));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar inscrição por ID: " + e.getMessage());
        }
        return inscricao;
    }

    public List<Inscricao> listarPorParticipante(int participanteId) {
        String sql = "SELECT * FROM inscricao WHERE participante_id = ?";
        List<Inscricao> inscricoes = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, participanteId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Inscricao inscricao = new Inscricao();
                    inscricao.setId(rs.getInt("id"));
                    inscricao.setParticipanteId(rs.getInt("participante_id"));
                    inscricao.setEventoId(rs.getInt("evento_id"));
                    
                    // Tratamento correto para valores NULL
                    int atividadeId = rs.getInt("atividade_id");
                    inscricao.setAtividadeId(rs.wasNull() ? 0 : atividadeId);
                    
                    inscricao.setDataInscricao(rs.getTimestamp("data_inscricao").toLocalDateTime());
                    inscricao.setStatusPagamento(StatusPagamento.valueOf(rs.getString("status_pagamento")));
                    inscricao.setValorPago(rs.getDouble("valor_pago"));
                    inscricoes.add(inscricao);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao listar inscrições do participante: " + e.getMessage());
        }
        return inscricoes;
    }

    public List<Inscricao> listarPorEvento(int eventoId) {
        String sql = "SELECT * FROM inscricao WHERE evento_id = ?";
        List<Inscricao> inscricoes = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, eventoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Inscricao inscricao = new Inscricao();
                    inscricao.setId(rs.getInt("id"));
                    inscricao.setParticipanteId(rs.getInt("participante_id"));
                    inscricao.setEventoId(rs.getInt("evento_id"));
                    
                    // Tratamento correto para valores NULL
                    int atividadeId = rs.getInt("atividade_id");
                    inscricao.setAtividadeId(rs.wasNull() ? 0 : atividadeId);
                    
                    inscricao.setDataInscricao(rs.getTimestamp("data_inscricao").toLocalDateTime());
                    inscricao.setStatusPagamento(StatusPagamento.valueOf(rs.getString("status_pagamento")));
                    inscricao.setValorPago(rs.getDouble("valor_pago"));
                    inscricoes.add(inscricao);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao listar inscrições do evento: " + e.getMessage());
        }
        return inscricoes;
    }

    public void atualizar(Inscricao inscricao) {
        String sql = "UPDATE inscricao SET participante_id = ?, evento_id = ?, atividade_id = ?, data_inscricao = ?, status_pagamento = ?, valor_pago = ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, inscricao.getParticipanteId());
            ps.setInt(2, inscricao.getEventoId());
            if (inscricao.getAtividadeId() == 0) {
                ps.setNull(3, Types.INTEGER);
            } else {
                ps.setInt(3, inscricao.getAtividadeId());
            }
            ps.setTimestamp(4, Timestamp.valueOf(inscricao.getDataInscricao()));
            ps.setString(5, inscricao.getStatusPagamento().toString());
            ps.setDouble(6, inscricao.getValorPago());
            ps.setInt(7, inscricao.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao atualizar inscrição: " + e.getMessage());
        }
    }

    public void excluir(int id) {
        String sql = "DELETE FROM inscricao WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao excluir inscrição: " + e.getMessage());
        }
    }

    public int contarInscricoesPorAtividade(int atividadeId) {
        String sql = "SELECT COUNT(*) AS total FROM inscricao WHERE atividade_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, atividadeId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt("total") : 0;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao contar inscrições: " + e.getMessage());
        }
    }

    public int contarInscricoesPorEvento(int eventoId) {
        String sql = "SELECT COUNT(*) AS total FROM inscricao WHERE evento_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, eventoId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt("total") : 0;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao contar inscrições do evento: " + e.getMessage());
        }
    }

    public boolean verificarInscricaoDuplicada(int participanteId, int eventoId, int atividadeId) {
        String sql = "SELECT COUNT(*) AS total FROM inscricao WHERE participante_id = ? AND evento_id = ?";
        
        if (atividadeId > 0) {
            sql += " AND atividade_id = ?";
        } else {
            sql += " AND (atividade_id IS NULL OR atividade_id = 0)";
        }

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, participanteId);
            ps.setInt(2, eventoId);
            
            if (atividadeId > 0) {
                ps.setInt(3, atividadeId);
            }

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt("total") > 0;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao verificar inscrição duplicada: " + e.getMessage());
        }
    }

    public List<Inscricao> listarTodos() {
        String sql = "SELECT * FROM inscricao";
        List<Inscricao> inscricoes = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Inscricao inscricao = new Inscricao();
                inscricao.setId(rs.getInt("id"));
                inscricao.setParticipanteId(rs.getInt("participante_id"));
                inscricao.setEventoId(rs.getInt("evento_id"));
                
                // Tratamento correto para valores NULL
                int atividadeId = rs.getInt("atividade_id");
                inscricao.setAtividadeId(rs.wasNull() ? 0 : atividadeId);
                
                inscricao.setDataInscricao(rs.getTimestamp("data_inscricao").toLocalDateTime());
                inscricao.setStatusPagamento(StatusPagamento.valueOf(rs.getString("status_pagamento")));
                inscricao.setValorPago(rs.getDouble("valor_pago"));
                inscricoes.add(inscricao);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao listar inscrições: " + e.getMessage());
        }
        return inscricoes;
    }

    public List<Inscricao> buscarPorParticipante(int participanteId) {
        return listarPorParticipante(participanteId);
    }
}