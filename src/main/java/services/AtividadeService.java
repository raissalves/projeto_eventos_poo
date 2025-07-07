package services;

import dao.AtividadeDAO;
import model.Atividade;
import exceptions.DatabaseException;

import java.util.List;

public class AtividadeService {
    private final AtividadeDAO atividadeDAO;

    public AtividadeService(AtividadeDAO atividadeDAO) {
        this.atividadeDAO = atividadeDAO;
    }

    public void criarAtividade(Atividade atividade) {
        atividadeDAO.inserir(atividade);
    }

    public List<Atividade> listarAtividadesPorEvento(int eventoId) {
        return atividadeDAO.listarPorEvento(eventoId);
    }

    // Outros métodos: atualizarAtividade, excluirAtividade, etc.
}