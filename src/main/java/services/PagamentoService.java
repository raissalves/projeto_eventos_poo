package services;

import dao.InscricaoDAO;
import model.Inscricao;
import enums.StatusPagamento;
import exceptions.PagamentoInvalidoException;

/**
 * Serviço responsável por processar pagamentos de inscrições
 */
public class PagamentoService {
    private final InscricaoDAO inscricaoDAO;

    public PagamentoService() {
        this.inscricaoDAO = new InscricaoDAO();
    }

    /**
     * Processa o pagamento de uma inscrição
     * @param inscricaoId ID da inscrição
     * @param valor Valor do pagamento
     * @throws PagamentoInvalidoException se o pagamento for inválido
     */
    public void processarPagamento(int inscricaoId, double valor) throws PagamentoInvalidoException {
        if (valor <= 0) {
            throw new PagamentoInvalidoException("Valor do pagamento deve ser maior que zero");
        }

        Inscricao inscricao = inscricaoDAO.buscarPorId(inscricaoId);
        if (inscricao == null) {
            throw new PagamentoInvalidoException("Inscrição não encontrada");
        }

        // Atualizar status e valor do pagamento
        inscricao.setStatusPagamento(StatusPagamento.CONFIRMADO);
        inscricao.setValorPago(valor);
        
        inscricaoDAO.atualizar(inscricao);
    }
}
