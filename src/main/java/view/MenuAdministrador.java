package view;

import dao.UsuarioDAO;
import dao.EventoDAO;
import dao.AtividadeDAO;
import dao.InscricaoDAO;
import dao.ValorInscricaoDAO;
import model.Usuario;
import model.Inscricao;
import enums.TipoUsuario;

import java.util.List;

/**
 * Menu principal do administrador com todas as funcionalidades administrativas
 */
public class MenuAdministrador extends Menu {
    private UsuarioDAO usuarioDAO;
    private EventoDAO eventoDAO;
    private AtividadeDAO atividadeDAO;
    private InscricaoDAO inscricaoDAO;
    private ValorInscricaoDAO valorInscricaoDAO;

    public MenuAdministrador(ConsoleInterface consoleInterface) {
        super(consoleInterface);
        this.usuarioDAO = new UsuarioDAO();
        this.eventoDAO = new EventoDAO();
        this.atividadeDAO = new AtividadeDAO();
        this.inscricaoDAO = new InscricaoDAO();
        this.valorInscricaoDAO = new ValorInscricaoDAO();
    }

    @Override
    protected void exibirMenu() {
        Usuario usuario = consoleInterface.getUsuarioLogado();
        exibirCabecalho("👨‍💼 MENU DO ADMINISTRADOR");
        System.out.println("Bem-vindo, " + usuario.getNome() + "!");
        System.out.println("1. Gerenciar eventos");
        System.out.println("2. Gerenciar atividades");
        System.out.println("3. Gerenciar usuários");
        System.out.println("4. Visualizar inscrições");
        System.out.println("5. Gerenciar valores de inscrição");
        System.out.println("6. Relatórios");
        System.out.println("7. Fazer logout");
        System.out.print("Escolha uma opção: ");
    }

    @Override
    protected void processarOpcao(int opcao) {
        switch (opcao) {
            case 1:
                new MenuGerenciarEventos(consoleInterface).executar();
                break;
            case 2:
                new MenuGerenciarAtividades(consoleInterface).executar();
                break;
            case 3:
                new MenuGerenciarUsuarios(consoleInterface).executar();
                break;
            case 4:
                visualizarInscricoes();
                break;
            case 5:
                new MenuGerenciarValoresInscricao(consoleInterface).executar();
                break;
            case 6:
                new MenuRelatorios(consoleInterface).executar();
                break;
            case 7:
                fazerLogout();
                break;
            default:
                exibirErro("Opção inválida!");
        }
    }

    private void visualizarInscricoes() {
        exibirCabecalho("📋 VISUALIZAR INSCRIÇÕES");
        try {
            List<Inscricao> inscricoes = inscricaoDAO.listarTodos();
            if (inscricoes.isEmpty()) {
                exibirInfo("Nenhuma inscrição encontrada.");
            } else {
                for (Inscricao inscricao : inscricoes) {
                    System.out.println("ID da Inscrição: " + inscricao.getId());
                    System.out.println("Participante ID: " + inscricao.getParticipanteId());
                    System.out.println("Evento ID: " + inscricao.getEventoId());
                    System.out.println("Atividade ID: " + (inscricao.getAtividadeId() != 0 ? inscricao.getAtividadeId() : "N/A"));
                    System.out.println("Data de Inscrição: " + inscricao.getDataInscricao());
                    System.out.println("Status do Pagamento: " + inscricao.getStatusPagamento());
                    System.out.println("Valor Pago: " + (inscricao.getValorPago() > 0 ? "R$ " + inscricao.getValorPago() : "N/A"));
                    System.out.println("---");
                }
            }
        } catch (Exception e) {
            exibirErro("Erro ao listar inscrições: " + e.getMessage());
        }
    }

    private void fazerLogout() {
        consoleInterface.setUsuarioLogado(null);
        exibirSucesso("Logout realizado com sucesso!");
    }
} 