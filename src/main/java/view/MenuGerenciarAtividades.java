package view;

import dao.AtividadeDAO;
import model.Atividade;
import enums.TipoAtividade;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Menu para gerenciar atividades
 */
public class MenuGerenciarAtividades extends Menu {
    private AtividadeDAO atividadeDAO;

    public MenuGerenciarAtividades(ConsoleInterface consoleInterface) {
        super(consoleInterface);
        this.atividadeDAO = new AtividadeDAO();
    }

    @Override
    protected void exibirMenu() {
        exibirCabecalho("🎯 GERENCIAR ATIVIDADES");
        System.out.println("1. Criar nova atividade");
        System.out.println("2. Listar atividades");
        System.out.println("3. Editar atividade");
        System.out.println("4. Excluir atividade");
        exibirRodape();
    }

    @Override
    protected void processarOpcao(int opcao) {
        switch (opcao) {
            case 1:
                criarAtividade();
                break;
            case 2:
                listarAtividades();
                break;
            case 3:
                editarAtividade();
                break;
            case 4:
                excluirAtividade();
                break;
            case 0:
                return;
            default:
                exibirErro("Opção inválida!");
        }
    }

    private void criarAtividade() {
        exibirCabecalho("🎯 CRIAR NOVA ATIVIDADE");
        
        try {
            // Primeiro, mostrar eventos disponíveis
            dao.EventoDAO eventoDAO = new dao.EventoDAO();
            List<model.Evento> eventos = eventoDAO.listarTodos();
            if (eventos.isEmpty()) {
                exibirInfo("Nenhum evento disponível. Crie um evento primeiro.");
                return;
            }
            
            System.out.println("Eventos disponíveis:");
            for (model.Evento evento : eventos) {
                System.out.println("ID: " + evento.getId() + " - " + evento.getTitulo());
            }
            
            System.out.print("\nID do evento: ");
            int eventoId = lerInteiro();
            
            // Verificar se o evento existe
            model.Evento evento = eventoDAO.buscarPorId(eventoId);
            if (evento == null) {
                exibirErro("Evento não encontrado!");
                return;
            }
            
            System.out.print("Nome da atividade: ");
            String nome = lerString();
            System.out.print("Descrição: ");
            String descricao = lerString();
            System.out.print("Data de realização (dd/MM/yyyy HH:mm): ");
            String dataRealizacaoStr = lerString();
            System.out.print("Limite de inscritos: ");
            int limiteInscritos = lerInteiro();
            System.out.print("Tipo (1-PALESTRA, 2-SIMPOSIO, 3-CURSO, 4-WORKSHOP): ");
            int tipoOpcao = lerInteiro();

            // Validar dados
            if (nome.trim().isEmpty()) {
                exibirErro("Nome da atividade não pode estar vazio!");
                return;
            }
            
            if (limiteInscritos <= 0) {
                exibirErro("Limite de inscritos deve ser maior que zero!");
                return;
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            LocalDateTime dataRealizacao = LocalDateTime.parse(dataRealizacaoStr, formatter);

            TipoAtividade tipo = switch (tipoOpcao) {
                case 1 -> TipoAtividade.PALESTRA;
                case 2 -> TipoAtividade.SIMPOSIO;
                case 3 -> TipoAtividade.CURSO;
                case 4 -> TipoAtividade.WORKSHOP;
                default -> TipoAtividade.PALESTRA;
            };

            Atividade atividade = new Atividade(nome, descricao, dataRealizacao, limiteInscritos, tipo, eventoId);
            atividadeDAO.inserir(atividade);
            exibirSucesso("Atividade criada com sucesso no evento '" + evento.getTitulo() + "'!");
        } catch (Exception e) {
            exibirErro("Erro ao criar atividade: " + e.getMessage());
        }
    }

    private void listarAtividades() {
        exibirCabecalho("🎯 LISTA DE ATIVIDADES");
        try {
            List<Atividade> atividades = atividadeDAO.listarTodos();
            if (atividades.isEmpty()) {
                exibirInfo("Nenhuma atividade encontrada.");
            } else {
                dao.EventoDAO eventoDAO = new dao.EventoDAO();
                dao.InscricaoDAO inscricaoDAO = new dao.InscricaoDAO();
                
                for (Atividade atividade : atividades) {
                    System.out.println("ID: " + atividade.getId());
                    System.out.println("Nome: " + atividade.getNome());
                    System.out.println("Descrição: " + atividade.getDescricao());
                    System.out.println("Data de Realização: " + atividade.getDataRealizacao());
                    System.out.println("Limite de Inscritos: " + atividade.getLimiteInscritos());
                    System.out.println("Tipo: " + atividade.getTipo());
                    
                    // Buscar informações do evento
                    model.Evento evento = eventoDAO.buscarPorId(atividade.getEventoId());
                    System.out.println("Evento: " + (evento != null ? evento.getTitulo() : "N/A"));
                    
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

    private void editarAtividade() {
        exibirCabecalho("📝 EDITAR ATIVIDADE");
        
        try {
            // Primeiro, listar as atividades disponíveis
            List<Atividade> atividades = atividadeDAO.listarTodos();
            if (atividades.isEmpty()) {
                exibirInfo("Nenhuma atividade encontrada para editar.");
                return;
            }
            
            System.out.println("Atividades disponíveis:");
            for (Atividade atividade : atividades) {
                System.out.println("ID: " + atividade.getId() + " - " + atividade.getNome());
            }
            
            System.out.print("\nDigite o ID da atividade que deseja editar: ");
            int atividadeId = lerInteiro();
            
            Atividade atividade = atividadeDAO.buscarPorId(atividadeId);
            if (atividade == null) {
                exibirErro("Atividade não encontrada!");
                return;
            }
            
            // Mostrar dados atuais
            System.out.println("\nDados atuais da atividade:");
            System.out.println("Nome: " + atividade.getNome());
            System.out.println("Descrição: " + atividade.getDescricao());
            System.out.println("Data de Realização: " + atividade.getDataRealizacao());
            System.out.println("Limite de Inscritos: " + atividade.getLimiteInscritos());
            System.out.println("Tipo: " + atividade.getTipo());
            System.out.println("Evento ID: " + atividade.getEventoId());
            
            // Solicitar novos dados
            System.out.println("\nDigite os novos dados (pressione Enter para manter o valor atual):");
            
            System.out.print("Novo nome [" + atividade.getNome() + "]: ");
            String novoNome = lerString();
            if (!novoNome.trim().isEmpty()) {
                atividade.setNome(novoNome);
            }
            
            System.out.print("Nova descrição [" + atividade.getDescricao() + "]: ");
            String novaDescricao = lerString();
            if (!novaDescricao.trim().isEmpty()) {
                atividade.setDescricao(novaDescricao);
            }
            
            System.out.print("Nova data de realização [" + atividade.getDataRealizacao() + "] (dd/MM/yyyy HH:mm): ");
            String novaDataRealizacaoStr = lerString();
            if (!novaDataRealizacaoStr.trim().isEmpty()) {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                    LocalDateTime novaDataRealizacao = LocalDateTime.parse(novaDataRealizacaoStr, formatter);
                    atividade.setDataRealizacao(novaDataRealizacao);
                } catch (Exception e) {
                    exibirErro("Formato de data inválido! Mantendo data atual.");
                }
            }
            
            System.out.print("Novo limite de inscritos [" + atividade.getLimiteInscritos() + "]: ");
            String novoLimiteStr = lerString();
            if (!novoLimiteStr.trim().isEmpty()) {
                try {
                    int novoLimite = Integer.parseInt(novoLimiteStr);
                    if (novoLimite > 0) {
                        atividade.setLimiteInscritos(novoLimite);
                    } else {
                        exibirErro("Limite deve ser maior que zero! Mantendo valor atual.");
                    }
                } catch (NumberFormatException e) {
                    exibirErro("Valor inválido! Mantendo valor atual.");
                }
            }
            
            System.out.print("Novo tipo [" + atividade.getTipo() + "] (1-PALESTRA, 2-SIMPOSIO, 3-CURSO, 4-WORKSHOP): ");
            String novoTipoStr = lerString();
            if (!novoTipoStr.trim().isEmpty()) {
                try {
                    int novoTipoOpcao = Integer.parseInt(novoTipoStr);
                    TipoAtividade novoTipo = switch (novoTipoOpcao) {
                        case 1 -> TipoAtividade.PALESTRA;
                        case 2 -> TipoAtividade.SIMPOSIO;
                        case 3 -> TipoAtividade.CURSO;
                        case 4 -> TipoAtividade.WORKSHOP;
                        default -> atividade.getTipo();
                    };
                    atividade.setTipo(novoTipo);
                } catch (NumberFormatException e) {
                    exibirErro("Valor inválido! Mantendo tipo atual.");
                }
            }
            
            // Confirmar edição
            System.out.println("\nDados atualizados:");
            System.out.println("Nome: " + atividade.getNome());
            System.out.println("Descrição: " + atividade.getDescricao());
            System.out.println("Data de Realização: " + atividade.getDataRealizacao());
            System.out.println("Limite de Inscritos: " + atividade.getLimiteInscritos());
            System.out.println("Tipo: " + atividade.getTipo());
            System.out.println("Evento ID: " + atividade.getEventoId());
            
            System.out.print("\nConfirmar edição? (s/n): ");
            String confirmacao = lerString();
            
            if (confirmacao.toLowerCase().startsWith("s")) {
                atividadeDAO.atualizar(atividade);
                exibirSucesso("Atividade atualizada com sucesso!");
            } else {
                exibirInfo("Edição cancelada.");
            }
            
        } catch (Exception e) {
            exibirErro("Erro ao editar atividade: " + e.getMessage());
        }
    }

    private void excluirAtividade() {
        exibirCabecalho("🗑️ EXCLUIR ATIVIDADE");
        
        try {
            // Primeiro, listar as atividades disponíveis
            List<Atividade> atividades = atividadeDAO.listarTodos();
            if (atividades.isEmpty()) {
                exibirInfo("Nenhuma atividade encontrada para excluir.");
                return;
            }
            
            System.out.println("Atividades disponíveis:");
            for (Atividade atividade : atividades) {
                System.out.println("ID: " + atividade.getId() + " - " + atividade.getNome());
            }
            
            System.out.print("\nDigite o ID da atividade que deseja excluir: ");
            int atividadeId = lerInteiro();
            
            Atividade atividade = atividadeDAO.buscarPorId(atividadeId);
            if (atividade == null) {
                exibirErro("Atividade não encontrada!");
                return;
            }
            
            // Verificar se há inscrições na atividade
            dao.InscricaoDAO inscricaoDAO = new dao.InscricaoDAO();
            int inscricoesAtividade = inscricaoDAO.contarInscricoesPorAtividade(atividadeId);
            
            // Buscar informações do evento
            dao.EventoDAO eventoDAO = new dao.EventoDAO();
            model.Evento evento = eventoDAO.buscarPorId(atividade.getEventoId());
            
            // Mostrar dados da atividade
            System.out.println("\nDados da atividade a ser excluída:");
            System.out.println("ID: " + atividade.getId());
            System.out.println("Nome: " + atividade.getNome());
            System.out.println("Descrição: " + atividade.getDescricao());
            System.out.println("Data de Realização: " + atividade.getDataRealizacao());
            System.out.println("Limite de Inscritos: " + atividade.getLimiteInscritos());
            System.out.println("Tipo: " + atividade.getTipo());
            System.out.println("Evento: " + (evento != null ? evento.getTitulo() : "N/A"));
            System.out.println("Quantidade de Inscrições: " + inscricoesAtividade);
            
            // Avisar sobre consequências
            if (inscricoesAtividade > 0) {
                System.out.println("\n⚠️ ATENÇÃO:");
                System.out.println("- Esta atividade possui " + inscricoesAtividade + " inscrição(ões) que serão perdidas.");
                System.out.println("- Esta ação não pode ser desfeita!");
            }
            
            // Confirmar exclusão
            System.out.print("\nTem certeza que deseja excluir esta atividade? (s/n): ");
            String confirmacao = lerString();
            
            if (confirmacao.toLowerCase().startsWith("s")) {
                atividadeDAO.excluir(atividadeId);
                exibirSucesso("Atividade excluída com sucesso!");
            } else {
                exibirInfo("Exclusão cancelada.");
            }
            
        } catch (Exception e) {
            exibirErro("Erro ao excluir atividade: " + e.getMessage());
        }
    }
} 