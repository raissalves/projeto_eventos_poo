package services;

import dao.EventoDAO;
import dao.ValorInscricaoDAO;
import model.Evento;
import model.ValorInscricao;
import exceptions.DatabaseException;

import java.util.List;

public class EventoService {
    private final EventoDAO eventoDAO;
    private final ValorInscricaoDAO valorInscricaoDAO;

    public EventoService(EventoDAO eventoDAO, ValorInscricaoDAO valorInscricaoDAO) {
        this.eventoDAO = eventoDAO;
        this.valorInscricaoDAO = valorInscricaoDAO;
    }

    public void criarEvento(Evento evento, List<ValorInscricao> valoresInscricao) {
        try {
            // Persiste o evento
            eventoDAO.inserir(evento);

            // Persiste os valores de inscrição associados
            for (ValorInscricao valor : valoresInscricao) {
                valor.setEventoId(evento.getId());
                valorInscricaoDAO.inserir(valor);
            }
        } catch (DatabaseException e) {
            throw new RuntimeException("Falha ao criar evento: " + e.getMessage(), e);
        }
    }

    public double obterValorInscricao(int eventoId, String perfil) {
        ValorInscricao valor = valorInscricaoDAO.buscarPorEventoEPerfil(eventoId, perfil);
        if (valor == null) {
            throw new IllegalArgumentException("Valor de inscrição não configurado para este perfil");
        }
        return valor.getValor();
    }

    // Outros métodos: atualizarEvento, excluirEvento, listarEventosPorAdministrador, etc.
}