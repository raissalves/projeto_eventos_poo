package view;

/**
 * Menu para gerenciar valores de inscrição
 */
public class MenuGerenciarValoresInscricao extends Menu {

    public MenuGerenciarValoresInscricao(ConsoleInterface consoleInterface) {
        super(consoleInterface);
    }

    @Override
    protected void exibirMenu() {
        exibirCabecalho("💰 GERENCIAR VALORES DE INSCRIÇÃO");
        System.out.println("1. Definir valor de inscrição");
        System.out.println("2. Listar valores de inscrição");
        System.out.println("3. Editar valor de inscrição");
        exibirRodape();
    }

    @Override
    protected void processarOpcao(int opcao) {
        switch (opcao) {
            case 1:
                definirValorInscricao();
                break;
            case 2:
                listarValoresInscricao();
                break;
            case 3:
                editarValorInscricao();
                break;
            case 0:
                return;
            default:
                exibirErro("Opção inválida!");
        }
    }

    private void definirValorInscricao() {
        exibirAviso("Funcionalidade em desenvolvimento...");
    }

    private void listarValoresInscricao() {
        exibirAviso("Funcionalidade em desenvolvimento...");
    }

    private void editarValorInscricao() {
        exibirAviso("Funcionalidade em desenvolvimento...");
    }
} 