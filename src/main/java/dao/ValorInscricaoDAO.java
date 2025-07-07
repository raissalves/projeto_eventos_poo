package dao;

import config.DatabaseConfig;
import enums.PerfilParticipante;
import model.ValorInscricao;
import exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ValorInscricaoDAO {

    public void inserir(ValorInscricao valorInscricao) {
        String sql = "INSERT INTO valor_inscricao (evento_id, perfil, valor) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, valorInscricao.getEventoId());
            ps.setString(2, valorInscricao.getPerfil().name());  // Convertendo enum para string
            ps.setDouble(3, valorInscricao.getValor());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao inserir valor de inscrição: " + e.getMessage());
        }
    }

    public ValorInscricao buscarPorEventoEPerfil(int eventoId, String perfil) {
        String sql = "SELECT * FROM valor_inscricao WHERE evento_id = ? AND perfil = ?";
        ValorInscricao valorInscricao = null;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, eventoId);
            ps.setString(2, perfil);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    valorInscricao = new ValorInscricao();
                    valorInscricao.setEventoId(rs.getInt("evento_id"));
                    valorInscricao.setPerfil(PerfilParticipante.valueOf(rs.getString("perfil")));
                    valorInscricao.setValor(rs.getDouble("valor"));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar valor de inscrição: " + e.getMessage());
        }
        return valorInscricao;
    }

    public List<ValorInscricao> listarPorEvento(int eventoId) {
        String sql = "SELECT * FROM valor_inscricao WHERE evento_id = ?";
        List<ValorInscricao> valores = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, eventoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ValorInscricao valor = new ValorInscricao();
                    valor.setEventoId(rs.getInt("evento_id"));
                    valor.setPerfil(PerfilParticipante.valueOf(rs.getString("perfil")));
                    valor.setValor(rs.getDouble("valor"));
                    valores.add(valor);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao listar valores de inscrição do evento: " + e.getMessage());
        }
        return valores;
    }

    public void atualizar(ValorInscricao valorInscricao) {
        String sql = "UPDATE valor_inscricao SET valor = ? WHERE evento_id = ? AND perfil = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, valorInscricao.getValor());
            ps.setInt(2, valorInscricao.getEventoId());
            ps.setString(3, valorInscricao.getPerfil().name());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao atualizar valor de inscrição: " + e.getMessage());
        }
    }

    public void excluir(int eventoId, String perfil) {
        String sql = "DELETE FROM valor_inscricao WHERE evento_id = ? AND perfil = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, eventoId);
            ps.setString(2, perfil);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao excluir valor de inscrição: " + e.getMessage());
        }
    }
}