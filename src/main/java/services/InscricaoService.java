package services;

import dao.InscricaoDAO;
import dao.AtividadeDAO;
import model.Inscricao;
import enums.StatusPagamento;
import exceptions.AtividadeLotadaException;
import exceptions.InscricaoDuplicadaException;
import exceptions.PagamentoInvalidoException;

import java.time.LocalDateTime;

public class InscricaoService {
    private final InscricaoDAO inscricaoDAO;
    private final AtividadeDAO atividadeDAO;

    public InscricaoService(InscricaoDAO inscricaoDAO, AtividadeDAO atividadeDAO) {
        this.inscricaoDAO = inscricaoDAO;
        this.atividadeDAO = atividadeDAO;
    }

    public void realizarInscricao(Inscricao inscricao) throws AtividadeLotadaException {
        // Verificar duplicidade de inscrição
        if (isInscricaoDuplicada(inscricao)) {
            throw new InscricaoDuplicadaException(
                    "Usuário já inscrito nesta " +
                            (inscricao.getAtividadeId() > 0 ? "atividade" : "evento")
            );
        }

        // Verificar limite de vagas para atividades
        if (inscricao.getAtividadeId() > 0) {
            verificarLimiteVagas(inscricao.getAtividadeId());
        }

        // Configurar dados padrão
        inscricao.setDataInscricao(LocalDateTime.now());
        inscricao.setStatusPagamento(StatusPagamento.PENDENTE);

        // Persistir inscrição
        inscricaoDAO.inserir(inscricao);
    }

    public void confirmarPagamento(int inscricaoId) {
        Inscricao inscricao = inscricaoDAO.buscarPorId(inscricaoId);

        if (inscricao == null) {
            throw new PagamentoInvalidoException("Inscrição não encontrada");
        }

        if (inscricao.getStatusPagamento() != StatusPagamento.PENDENTE) {
            throw new PagamentoInvalidoException("Pagamento já foi processado anteriormente");
        }

        inscricao.setStatusPagamento(StatusPagamento.CONFIRMADO);
        inscricaoDAO.atualizar(inscricao);
    }

    public void cancelarInscricao(int inscricaoId) {
        Inscricao inscricao = inscricaoDAO.buscarPorId(inscricaoId);

        if (inscricao == null) {
            throw new IllegalArgumentException("Inscrição não encontrada");
        }

        if (inscricao.getStatusPagamento() == StatusPagamento.CONFIRMADO) {
            throw new IllegalStateException("Não é possível cancelar inscrição com pagamento confirmado");
        }

        inscricaoDAO.excluir(inscricaoId);
    }

    private boolean isInscricaoDuplicada(Inscricao novaInscricao) {
        return inscricaoDAO.verificarInscricaoDuplicada(
                novaInscricao.getParticipanteId(),
                novaInscricao.getEventoId(),
                novaInscricao.getAtividadeId()
        );
    }

    private void verificarLimiteVagas(int atividadeId) throws AtividadeLotadaException {
        int vagasOcupadas = inscricaoDAO.contarInscricoesPorAtividade(atividadeId);
        int limite = atividadeDAO.buscarPorId(atividadeId).getLimiteInscritos();

        if (vagasOcupadas >= limite) {
            throw new AtividadeLotadaException("Atividade atingiu o limite de inscritos");
        }
    }
}