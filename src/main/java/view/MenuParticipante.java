package view;

import dao.EventoDAO;
import dao.InscricaoDAO;
import dao.AtividadeDAO;
import model.Usuario;
import model.Evento;
import model.Inscricao;
import model.Atividade;
import services.InscricaoService;
import services.PagamentoService;
import exceptions.AtividadeLotadaException;
import exceptions.InscricaoDuplicadaException;

import java.util.List;

/**
 * Menu principal do participante com funcionalidades específicas
 */
public class MenuParticipante extends Menu {
    private EventoDAO eventoDAO;
    private InscricaoDAO inscricaoDAO;
    private AtividadeDAO atividadeDAO;
    private InscricaoService inscricaoService;
    private PagamentoService pagamentoService;

    public MenuParticipante(ConsoleInterface consoleInterface) {
        super(consoleInterface);
        this.eventoDAO = new EventoDAO();
        this.inscricaoDAO = new InscricaoDAO();
        this.atividadeDAO = new AtividadeDAO();
        this.inscricaoService = new InscricaoService(inscricaoDAO, atividadeDAO);
        this.pagamentoService = new PagamentoService();
    }

    @Override
    protected void exibirMenu() {
        Usuario usuario = consoleInterface.getUsuarioLogado();
        exibirCabecalho("👤 MENU DO PARTICIPANTE");
        System.out.println("Bem-vindo, " + usuario.getNome() + "!");
        System.out.println("1. Visualizar eventos disponíveis");
        System.out.println("2. Visualizar atividades de um evento");
        System.out.println("3. Inscrever-se em evento");
        System.out.println("4. Inscrever-se em atividade específica");
        System.out.println("5. Visualizar minhas inscrições");
        System.out.println("6. Realizar pagamento");
        System.out.println("7. Fazer logout");
        System.out.print("Escolha uma opção: ");
    }

    @Override
    protected void processarOpcao(int opcao) {
        switch (opcao) {
            case 1:
                visualizarEventosDisponiveis();
                break;
            case 2:
                visualizarAtividadesDeEvento();
                break;
            case 3:
                inscreverEmEvento();
                break;
            case 4:
                inscreverEmAtividade();
                break;
            case 5:
                visualizarMinhasInscricoes();
                break;
            case 6:
                realizarPagamento();
                break;
            case 7:
                fazerLogout();
                break;
            default:
                exibirErro("Opção inválida!");
        }
    }

    private void visualizarEventosDisponiveis() {
        exibirCabecalho("📅 EVENTOS DISPONÍVEIS");
        try {
            List<Evento> eventos = eventoDAO.listarTodos();
            if (eventos.isEmpty()) {
                exibirInfo("Nenhum evento disponível.");
            } else {
                for (Evento evento : eventos) {
                    System.out.println("ID: " + evento.getId());
                    System.out.println("Título: " + evento.getTitulo());
                    System.out.println("Descrição: " + evento.getDescricao());
                    System.out.println("Data Início: " + evento.getDataInicio());
                    System.out.println("Data Fim: " + evento.getDataFim());
                    System.out.println("Localização: " + evento.getLocalizacao());
                    System.out.println("---");
                }
            }
        } catch (Exception e) {
            exibirErro("Erro ao listar eventos: " + e.getMessage());
        }
    }

    private void visualizarAtividadesDeEvento() {
        exibirCabecalho("🎯 ATIVIDADES DO EVENTO");
        
        try {
            // Primeiro, mostrar eventos disponíveis
            List<Evento> eventos = eventoDAO.listarTodos();
            if (eventos.isEmpty()) {
                exibirInfo("Nenhum evento disponível.");
                return;
            }
            
            System.out.println("Eventos disponíveis:");
            for (Evento evento : eventos) {
                System.out.println("ID: " + evento.getId() + " - " + evento.getTitulo());
            }
            
            System.out.print("\nDigite o ID do evento para ver suas atividades: ");
            int eventoId = lerInteiro();
            
            // Verificar se o evento existe
            Evento evento = eventoDAO.buscarPorId(eventoId);
            if (evento == null) {
                exibirErro("Evento não encontrado!");
                return;
            }
            
            // Listar atividades do evento
            List<Atividade> atividades = atividadeDAO.listarPorEvento(eventoId);
            if (atividades.isEmpty()) {
                exibirInfo("Este evento não possui atividades cadastradas.");
            } else {
                System.out.println("\nAtividades do evento '" + evento.getTitulo() + "':");
                for (Atividade atividade : atividades) {
                    System.out.println("ID: " + atividade.getId());
                    System.out.println("Nome: " + atividade.getNome());
                    System.out.println("Descrição: " + atividade.getDescricao());
                    System.out.println("Data de Realização: " + atividade.getDataRealizacao());
                    System.out.println("Tipo: " + atividade.getTipo());
                    System.out.println("Limite de Inscritos: " + atividade.getLimiteInscritos());
                    
                    // Mostrar vagas disponíveis
                    int inscritosAtuais = inscricaoDAO.contarInscricoesPorAtividade(atividade.getId());
                    int vagasDisponiveis = atividade.getLimiteInscritos() - inscritosAtuais;
                    System.out.println("Vagas Disponíveis: " + vagasDisponiveis + "/" + atividade.getLimiteInscritos());
                    System.out.println("---");
                }
            }
        } catch (Exception e) {
            exibirErro("Erro ao listar atividades: " + e.getMessage());
        }
    }

    private void inscreverEmEvento() {
        exibirCabecalho("📝 INSCRIÇÃO EM EVENTO");
        
        try {
            // Mostrar eventos disponíveis
            List<Evento> eventos = eventoDAO.listarTodos();
            if (eventos.isEmpty()) {
                exibirInfo("Nenhum evento disponível para inscrição.");
                return;
            }
            
            System.out.println("Eventos disponíveis:");
            for (Evento evento : eventos) {
                System.out.println("ID: " + evento.getId() + " - " + evento.getTitulo());
            }
            
            System.out.print("\nID do evento: ");
            int eventoId = lerInteiro();
            
            // Verificar se o evento existe
            Evento evento = eventoDAO.buscarPorId(eventoId);
            if (evento == null) {
                exibirErro("Evento não encontrado!");
                return;
            }
            
            // Criar inscrição apenas no evento (sem atividade específica)
            Inscricao inscricao = new Inscricao();
            inscricao.setParticipanteId(consoleInterface.getUsuarioLogado().getId());
            inscricao.setEventoId(eventoId);
            inscricao.setAtividadeId(0); // 0 indica inscrição apenas no evento
            
            // Usar o service para realizar a inscrição
            inscricaoService.realizarInscricao(inscricao);
            exibirSucesso("Inscrição no evento realizada com sucesso! ID da inscrição: " + inscricao.getId());
            
        } catch (InscricaoDuplicadaException e) {
            exibirErro("Você já está inscrito neste evento!");
        } catch (Exception e) {
            exibirErro("Erro ao realizar inscrição: " + e.getMessage());
        }
    }

    private void inscreverEmAtividade() {
        exibirCabecalho("🎯 INSCRIÇÃO EM ATIVIDADE");
        
        try {
            // Primeiro, mostrar eventos disponíveis
            List<Evento> eventos = eventoDAO.listarTodos();
            if (eventos.isEmpty()) {
                exibirInfo("Nenhum evento disponível.");
                return;
            }
            
            System.out.println("Eventos disponíveis:");
            for (Evento evento : eventos) {
                System.out.println("ID: " + evento.getId() + " - " + evento.getTitulo());
            }
            
            System.out.print("\nID do evento: ");
            int eventoId = lerInteiro();
            
            // Verificar se o evento existe
            Evento evento = eventoDAO.buscarPorId(eventoId);
            if (evento == null) {
                exibirErro("Evento não encontrado!");
                return;
            }
            
            // Listar atividades do evento
            List<Atividade> atividades = atividadeDAO.listarPorEvento(eventoId);
            if (atividades.isEmpty()) {
                exibirInfo("Este evento não possui atividades cadastradas.");
                return;
            }
            
            System.out.println("\nAtividades disponíveis:");
            for (Atividade atividade : atividades) {
                int inscritosAtuais = inscricaoDAO.contarInscricoesPorAtividade(atividade.getId());
                int vagasDisponiveis = atividade.getLimiteInscritos() - inscritosAtuais;
                System.out.println("ID: " + atividade.getId() + " - " + atividade.getNome() + 
                                 " (Vagas: " + vagasDisponiveis + "/" + atividade.getLimiteInscritos() + ")");
            }
            
            System.out.print("\nID da atividade: ");
            int atividadeId = lerInteiro();
            
            // Verificar se a atividade existe
            Atividade atividade = atividadeDAO.buscarPorId(atividadeId);
            if (atividade == null) {
                exibirErro("Atividade não encontrada!");
                return;
            }
            
            // Verificar se a atividade pertence ao evento selecionado
            if (atividade.getEventoId() != eventoId) {
                exibirErro("A atividade selecionada não pertence ao evento escolhido!");
                return;
            }
            
            // Criar inscrição na atividade específica
            Inscricao inscricao = new Inscricao();
            inscricao.setParticipanteId(consoleInterface.getUsuarioLogado().getId());
            inscricao.setEventoId(eventoId);
            inscricao.setAtividadeId(atividadeId);
            
            // Usar o service para realizar a inscrição (inclui verificação de limite)
            inscricaoService.realizarInscricao(inscricao);
            exibirSucesso("Inscrição na atividade realizada com sucesso! ID da inscrição: " + inscricao.getId());
            
        } catch (AtividadeLotadaException e) {
            exibirErro("Esta atividade está lotada! Não há vagas disponíveis.");
        } catch (InscricaoDuplicadaException e) {
            exibirErro("Você já está inscrito nesta atividade!");
        } catch (Exception e) {
            exibirErro("Erro ao realizar inscrição: " + e.getMessage());
        }
    }

    private void visualizarMinhasInscricoes() {
        exibirCabecalho("📋 MINHAS INSCRIÇÕES");
        try {
            List<Inscricao> inscricoes = inscricaoDAO.buscarPorParticipante(consoleInterface.getUsuarioLogado().getId());
            if (inscricoes.isEmpty()) {
                exibirInfo("Você não possui inscrições.");
            } else {
                for (Inscricao inscricao : inscricoes) {
                    System.out.println("ID da Inscrição: " + inscricao.getId());
                    
                    // Buscar informações do evento
                    Evento evento = eventoDAO.buscarPorId(inscricao.getEventoId());
                    System.out.println("Evento: " + (evento != null ? evento.getTitulo() : "N/A"));
                    
                    // Buscar informações da atividade (se houver)
                    if (inscricao.getAtividadeId() != 0) {
                        Atividade atividade = atividadeDAO.buscarPorId(inscricao.getAtividadeId());
                        System.out.println("Atividade: " + (atividade != null ? atividade.getNome() : "N/A"));
                    } else {
                        System.out.println("Atividade: Inscrição apenas no evento");
                    }
                    
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

    private void realizarPagamento() {
        exibirCabecalho("💳 REALIZAR PAGAMENTO");
        
        try {
            // Primeiro, mostrar as inscrições do usuário
            List<Inscricao> inscricoes = inscricaoDAO.buscarPorParticipante(consoleInterface.getUsuarioLogado().getId());
            if (inscricoes.isEmpty()) {
                exibirInfo("Você não possui inscrições para pagar.");
                return;
            }
            
            System.out.println("Suas inscrições:");
            for (Inscricao inscricao : inscricoes) {
                Evento evento = eventoDAO.buscarPorId(inscricao.getEventoId());
                String eventoNome = evento != null ? evento.getTitulo() : "N/A";
                
                System.out.println("ID: " + inscricao.getId() + " - " + eventoNome + 
                                 " (Status: " + inscricao.getStatusPagamento() + ")");
            }
            
            System.out.print("\nID da inscrição: ");
            int inscricaoId = lerInteiro();
            System.out.print("Valor do pagamento: ");
            double valor = lerDouble();

            pagamentoService.processarPagamento(inscricaoId, valor);
            exibirSucesso("Pagamento processado com sucesso!");
        } catch (Exception e) {
            exibirErro("Erro ao processar pagamento: " + e.getMessage());
        }
    }

    private void fazerLogout() {
        consoleInterface.setUsuarioLogado(null);
        exibirSucesso("Logout realizado com sucesso!");
    }
} 