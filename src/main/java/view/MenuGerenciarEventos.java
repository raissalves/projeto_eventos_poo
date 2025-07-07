package view;

import dao.EventoDAO;
import model.Evento;
import model.Usuario;
import enums.TipoUsuario;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Menu para gerenciar eventos (criar, listar, editar, excluir)
 */
public class MenuGerenciarEventos extends Menu {
    private EventoDAO eventoDAO;

    public MenuGerenciarEventos(ConsoleInterface consoleInterface) {
        super(consoleInterface);
        this.eventoDAO = new EventoDAO();
    }

    @Override
    protected void exibirMenu() {
        exibirCabecalho("📅 GERENCIAR EVENTOS");
        System.out.println("1. Criar novo evento");
        System.out.println("2. Listar eventos");
        System.out.println("3. Visualizar atividades de um evento");
        System.out.println("4. Editar evento");
        System.out.println("5. Excluir evento");
        exibirRodape();
    }

    @Override
    protected void processarOpcao(int opcao) {
        switch (opcao) {
            case 1:
                criarEvento();
                break;
            case 2:
                listarEventos();
                break;
            case 3:
                visualizarAtividadesDoEvento();
                break;
            case 4:
                editarEvento();
                break;
            case 5:
                excluirEvento();
                break;
            case 0:
                return;
            default:
                exibirErro("Opção inválida!");
        }
    }

    private void criarEvento() {
        exibirCabecalho("📅 CRIAR NOVO EVENTO");
        System.out.print("Título: ");
        String titulo = lerString();
        System.out.print("Descrição: ");
        String descricao = lerString();
        System.out.print("Data de início (dd/MM/yyyy HH:mm): ");
        String dataInicioStr = lerString();
        System.out.print("Data de fim (dd/MM/yyyy HH:mm): ");
        String dataFimStr = lerString();
        System.out.print("Localização: ");
        String localizacao = lerString();

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            LocalDateTime dataInicio = LocalDateTime.parse(dataInicioStr, formatter);
            LocalDateTime dataFim = LocalDateTime.parse(dataFimStr, formatter);

            Usuario usuario = consoleInterface.getUsuarioLogado();
            Evento evento = new Evento(titulo, descricao, dataInicio, dataFim, localizacao, usuario.getId());
            eventoDAO.inserir(evento);
            exibirSucesso("Evento criado com sucesso!");
        } catch (Exception e) {
            exibirErro("Erro ao criar evento: " + e.getMessage());
        }
    }

    private void listarEventos() {
        exibirCabecalho("📅 LISTA DE EVENTOS");
        try {
            List<Evento> eventos = eventoDAO.listarTodos();
            if (eventos.isEmpty()) {
                exibirInfo("Nenhum evento encontrado.");
            } else {
                dao.AtividadeDAO atividadeDAO = new dao.AtividadeDAO();
                Usuario usuarioLogado = consoleInterface.getUsuarioLogado();
                
                System.out.println("Seus eventos:");
                for (Evento evento : eventos) {
                    // Mostrar apenas eventos criados pelo administrador logado
                    if (evento.getAdministradorId() == usuarioLogado.getId()) {
                        System.out.println("ID: " + evento.getId());
                        System.out.println("Título: " + evento.getTitulo());
                        System.out.println("Descrição: " + evento.getDescricao());
                        System.out.println("Data Início: " + evento.getDataInicio());
                        System.out.println("Data Fim: " + evento.getDataFim());
                        System.out.println("Localização: " + evento.getLocalizacao());
                        
                        // Contar atividades do evento
                        List<model.Atividade> atividades = atividadeDAO.listarPorEvento(evento.getId());
                        System.out.println("Quantidade de Atividades: " + atividades.size());
                        System.out.println("---");
                    }
                }
                
                // Verificar se há outros eventos
                boolean temOutrosEventos = eventos.stream()
                    .anyMatch(e -> e.getAdministradorId() != usuarioLogado.getId());
                
                if (temOutrosEventos) {
                    System.out.println("\nOutros eventos no sistema:");
                    for (Evento evento : eventos) {
                        if (evento.getAdministradorId() != usuarioLogado.getId()) {
                            System.out.println("ID: " + evento.getId() + " - " + evento.getTitulo() + " (Criado por outro administrador)");
                        }
                    }
                }
            }
        } catch (Exception e) {
            exibirErro("Erro ao listar eventos: " + e.getMessage());
        }
    }

    private void visualizarAtividadesDoEvento() {
        exibirCabecalho("🎯 ATIVIDADES DO EVENTO");
        
        try {
            // Primeiro, mostrar eventos disponíveis
            List<Evento> eventos = eventoDAO.listarTodos();
            if (eventos.isEmpty()) {
                exibirInfo("Nenhum evento disponível.");
                return;
            }
            
            Usuario usuarioLogado = consoleInterface.getUsuarioLogado();
            System.out.println("Seus eventos:");
            for (Evento evento : eventos) {
                if (evento.getAdministradorId() == usuarioLogado.getId()) {
                    System.out.println("ID: " + evento.getId() + " - " + evento.getTitulo());
                }
            }
            
            System.out.print("\nDigite o ID do evento para ver suas atividades: ");
            int eventoId = lerInteiro();
            
            // Verificar se o evento existe
            Evento evento = eventoDAO.buscarPorId(eventoId);
            if (evento == null) {
                exibirErro("Evento não encontrado!");
                return;
            }
            
            // Verificar se o administrador é o criador do evento
            if (evento.getAdministradorId() != usuarioLogado.getId()) {
                exibirErro("Você só pode visualizar atividades de eventos criados por você!");
                return;
            }
            
            // Listar atividades do evento
            dao.AtividadeDAO atividadeDAO = new dao.AtividadeDAO();
            dao.InscricaoDAO inscricaoDAO = new dao.InscricaoDAO();
            List<model.Atividade> atividades = atividadeDAO.listarPorEvento(eventoId);
            
            if (atividades.isEmpty()) {
                exibirInfo("Este evento não possui atividades cadastradas.");
            } else {
                System.out.println("\nAtividades do evento '" + evento.getTitulo() + "':");
                for (model.Atividade atividade : atividades) {
                    System.out.println("ID: " + atividade.getId());
                    System.out.println("Nome: " + atividade.getNome());
                    System.out.println("Descrição: " + atividade.getDescricao());
                    System.out.println("Data de Realização: " + atividade.getDataRealizacao());
                    System.out.println("Tipo: " + atividade.getTipo());
                    System.out.println("Limite de Inscritos: " + atividade.getLimiteInscritos());
                    
                    // Mostrar vagas ocupadas
                    int inscritosAtuais = inscricaoDAO.contarInscricoesPorAtividade(atividade.getId());
                    int vagasDisponiveis = atividade.getLimiteInscritos() - inscritosAtuais;
                    System.out.println("Vagas Ocupadas: " + inscritosAtuais + "/" + atividade.getLimiteInscritos());
                    System.out.println("Vagas Disponíveis: " + vagasDisponiveis);
                    System.out.println("---");
                }
            }
        } catch (Exception e) {
            exibirErro("Erro ao listar atividades: " + e.getMessage());
        }
    }

    private void editarEvento() {
        exibirCabecalho("📝 EDITAR EVENTO");
        
        // Primeiro, listar os eventos disponíveis
        try {
            List<Evento> eventos = eventoDAO.listarTodos();
            if (eventos.isEmpty()) {
                exibirInfo("Nenhum evento encontrado para editar.");
                return;
            }
            
            Usuario usuarioLogado = consoleInterface.getUsuarioLogado();
            System.out.println("Seus eventos:");
            for (Evento evento : eventos) {
                if (evento.getAdministradorId() == usuarioLogado.getId()) {
                    System.out.println("ID: " + evento.getId() + " - " + evento.getTitulo());
                }
            }
            
            System.out.print("\nDigite o ID do evento que deseja editar: ");
            int eventoId = lerInteiro();
            
            Evento evento = eventoDAO.buscarPorId(eventoId);
            if (evento == null) {
                exibirErro("Evento não encontrado!");
                return;
            }
            
            // Verificar se o administrador é o criador do evento
            if (evento.getAdministradorId() != usuarioLogado.getId()) {
                exibirErro("Você só pode editar eventos criados por você!");
                return;
            }
            
            // Mostrar dados atuais
            System.out.println("\nDados atuais do evento:");
            System.out.println("Título: " + evento.getTitulo());
            System.out.println("Descrição: " + evento.getDescricao());
            System.out.println("Data Início: " + evento.getDataInicio());
            System.out.println("Data Fim: " + evento.getDataFim());
            System.out.println("Localização: " + evento.getLocalizacao());
            
            // Solicitar novos dados
            System.out.println("\nDigite os novos dados (pressione Enter para manter o valor atual):");
            
            System.out.print("Novo título [" + evento.getTitulo() + "]: ");
            String novoTitulo = lerString();
            if (!novoTitulo.trim().isEmpty()) {
                evento.setTitulo(novoTitulo);
            }
            
            System.out.print("Nova descrição [" + evento.getDescricao() + "]: ");
            String novaDescricao = lerString();
            if (!novaDescricao.trim().isEmpty()) {
                evento.setDescricao(novaDescricao);
            }
            
            System.out.print("Nova data de início [" + evento.getDataInicio() + "] (dd/MM/yyyy HH:mm): ");
            String novaDataInicioStr = lerString();
            if (!novaDataInicioStr.trim().isEmpty()) {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                    LocalDateTime novaDataInicio = LocalDateTime.parse(novaDataInicioStr, formatter);
                    evento.setDataInicio(novaDataInicio);
                } catch (Exception e) {
                    exibirErro("Formato de data inválido! Mantendo data atual.");
                }
            }
            
            System.out.print("Nova data de fim [" + evento.getDataFim() + "] (dd/MM/yyyy HH:mm): ");
            String novaDataFimStr = lerString();
            if (!novaDataFimStr.trim().isEmpty()) {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                    LocalDateTime novaDataFim = LocalDateTime.parse(novaDataFimStr, formatter);
                    evento.setDataFim(novaDataFim);
                } catch (Exception e) {
                    exibirErro("Formato de data inválido! Mantendo data atual.");
                }
            }
            
            System.out.print("Nova localização [" + evento.getLocalizacao() + "]: ");
            String novaLocalizacao = lerString();
            if (!novaLocalizacao.trim().isEmpty()) {
                evento.setLocalizacao(novaLocalizacao);
            }
            
            // Confirmar edição
            System.out.println("\nDados atualizados:");
            System.out.println("Título: " + evento.getTitulo());
            System.out.println("Descrição: " + evento.getDescricao());
            System.out.println("Data Início: " + evento.getDataInicio());
            System.out.println("Data Fim: " + evento.getDataFim());
            System.out.println("Localização: " + evento.getLocalizacao());
            
            System.out.print("\nConfirmar edição? (s/n): ");
            String confirmacao = lerString();
            
            if (confirmacao.toLowerCase().startsWith("s")) {
                eventoDAO.atualizar(evento);
                exibirSucesso("Evento atualizado com sucesso!");
            } else {
                exibirInfo("Edição cancelada.");
            }
            
        } catch (Exception e) {
            exibirErro("Erro ao editar evento: " + e.getMessage());
        }
    }

    private void excluirEvento() {
        exibirCabecalho("🗑️ EXCLUIR EVENTO");
        
        try {
            List<Evento> eventos = eventoDAO.listarTodos();
            if (eventos.isEmpty()) {
                exibirInfo("Nenhum evento encontrado para excluir.");
                return;
            }
            
            System.out.println("Eventos disponíveis:");
            for (Evento evento : eventos) {
                System.out.println("ID: " + evento.getId() + " - " + evento.getTitulo());
            }
            
            System.out.print("\nDigite o ID do evento que deseja excluir: ");
            int eventoId = lerInteiro();
            
            Evento evento = eventoDAO.buscarPorId(eventoId);
            if (evento == null) {
                exibirErro("Evento não encontrado!");
                return;
            }
            
            // Verificar se o administrador é o criador do evento
            Usuario usuarioLogado = consoleInterface.getUsuarioLogado();
            if (evento.getAdministradorId() != usuarioLogado.getId()) {
                exibirErro("Você só pode excluir eventos criados por você!");
                return;
            }
            
            // Verificar se há inscrições no evento
            dao.InscricaoDAO inscricaoDAO = new dao.InscricaoDAO();
            int inscricoesEvento = inscricaoDAO.contarInscricoesPorEvento(eventoId);
            
            // Verificar se há atividades no evento
            dao.AtividadeDAO atividadeDAO = new dao.AtividadeDAO();
            List<model.Atividade> atividades = atividadeDAO.listarPorEvento(eventoId);
            
            // Mostrar dados do evento a ser excluído
            System.out.println("\nDados do evento a ser excluído:");
            System.out.println("ID: " + evento.getId());
            System.out.println("Título: " + evento.getTitulo());
            System.out.println("Descrição: " + evento.getDescricao());
            System.out.println("Data Início: " + evento.getDataInicio());
            System.out.println("Data Fim: " + evento.getDataFim());
            System.out.println("Localização: " + evento.getLocalizacao());
            System.out.println("Quantidade de Atividades: " + atividades.size());
            System.out.println("Quantidade de Inscrições: " + inscricoesEvento);
            
            // Avisar sobre consequências
            if (inscricoesEvento > 0 || !atividades.isEmpty()) {
                System.out.println("\n⚠️ ATENÇÃO:");
                if (inscricoesEvento > 0) {
                    System.out.println("- Este evento possui " + inscricoesEvento + " inscrição(ões) que serão perdidas.");
                }
                if (!atividades.isEmpty()) {
                    System.out.println("- Este evento possui " + atividades.size() + " atividade(s) que serão excluídas.");
                }
                System.out.println("- Esta ação não pode ser desfeita!");
            }
            
            // Confirmar exclusão
            System.out.print("\nConfirmar exclusão? (s/n): ");
            String confirmacao = lerString();
            
            if (confirmacao.toLowerCase().startsWith("s")) {
                eventoDAO.excluir(eventoId);
                exibirSucesso("Evento excluído com sucesso!");
            } else {
                exibirInfo("Exclusão cancelada.");
            }
            
        } catch (Exception e) {
            exibirErro("Erro ao excluir evento: " + e.getMessage());
        }
    }
} 