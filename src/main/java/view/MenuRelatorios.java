package view;

/**
 * Menu para relatórios
 */
public class MenuRelatorios extends Menu {

    public MenuRelatorios(ConsoleInterface consoleInterface) {
        super(consoleInterface);
    }

    @Override
    protected void exibirMenu() {
        exibirCabecalho("📊 RELATÓRIOS");
        System.out.println("1. Relatório de eventos");
        System.out.println("2. Relatório de inscrições");
        System.out.println("3. Relatório financeiro");
        exibirRodape();
    }

    @Override
    protected void processarOpcao(int opcao) {
        switch (opcao) {
            case 1:
                relatorioEventos();
                break;
            case 2:
                relatorioInscricoes();
                break;
            case 3:
                relatorioFinanceiro();
                break;
            case 0:
                return;
            default:
                exibirErro("Opção inválida!");
        }
    }

    private void relatorioEventos() {
        exibirAviso("Funcionalidade em desenvolvimento...");
    }

    private void relatorioInscricoes() {
        exibirAviso("Funcionalidade em desenvolvimento...");
    }

    private void relatorioFinanceiro() {
        exibirAviso("Funcionalidade em desenvolvimento...");
    }
} 