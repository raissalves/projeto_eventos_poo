package view;

import java.util.Scanner;

/**
 * Classe abstrata que define a estrutura base para todos os menus do sistema.
 * Segue o padrão Template Method para definir o comportamento comum dos menus.
 */
public abstract class Menu {
    protected Scanner scanner;
    protected ConsoleInterface consoleInterface;

    public Menu(ConsoleInterface consoleInterface) {
        this.consoleInterface = consoleInterface;
        this.scanner = consoleInterface.getScanner();
    }

    /**
     * Método template que define o fluxo padrão de execução do menu
     */
    public final void executar() {
        exibirMenu();
        int opcao = lerOpcao();
        processarOpcao(opcao);
    }

    /**
     * Exibe o cabeçalho do menu
     */
    protected void exibirCabecalho(String titulo) {
        System.out.println("\n" + titulo);
        System.out.println("=".repeat(titulo.length()));
    }

    /**
     * Exibe o rodapé do menu
     */
    protected void exibirRodape() {
        System.out.println("0. Voltar");
        System.out.print("Escolha uma opção: ");
    }

    /**
     * Lê a opção escolhida pelo usuário
     */
    protected int lerOpcao() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("❌ Por favor, digite um número válido: ");
            }
        }
    }

    /**
     * Lê um valor inteiro
     */
    protected int lerInteiro() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("❌ Por favor, digite um número válido: ");
            }
        }
    }

    /**
     * Lê um valor decimal
     */
    protected double lerDouble() {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("❌ Por favor, digite um número válido: ");
            }
        }
    }

    /**
     * Lê uma string
     */
    protected String lerString() {
        return scanner.nextLine();
    }

    /**
     * Exibe uma mensagem de sucesso
     */
    protected void exibirSucesso(String mensagem) {
        System.out.println("✅ " + mensagem);
    }

    /**
     * Exibe uma mensagem de erro
     */
    protected void exibirErro(String mensagem) {
        System.out.println("❌ " + mensagem);
    }

    /**
     * Exibe uma mensagem de informação
     */
    protected void exibirInfo(String mensagem) {
        System.out.println("ℹ️ " + mensagem);
    }

    /**
     * Exibe uma mensagem de aviso
     */
    protected void exibirAviso(String mensagem) {
        System.out.println("⚠️ " + mensagem);
    }

    /**
     * Método abstrato que deve ser implementado pelas subclasses
     * para exibir as opções específicas do menu
     */
    protected abstract void exibirMenu();

    /**
     * Método abstrato que deve ser implementado pelas subclasses
     * para processar a opção escolhida pelo usuário
     */
    protected abstract void processarOpcao(int opcao);
} 