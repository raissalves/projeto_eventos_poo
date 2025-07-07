package view;

import model.Usuario;
import enums.TipoUsuario;

import java.util.Scanner;

/**
 * Interface principal do console que gerencia a navegação entre menus
 * e mantém o estado do usuário logado
 */
public class ConsoleInterface {
    private Scanner scanner;
    private Usuario usuarioLogado;

    public ConsoleInterface() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Inicia o sistema e gerencia o fluxo principal
     */
    public void iniciar() {
        while (true) {
            if (usuarioLogado == null) {
                // Usuário não logado - mostrar menu de login
                MenuLogin menuLogin = new MenuLogin(this);
                menuLogin.executar();
            } else {
                // Usuário logado - mostrar menu específico do tipo de usuário
                if (usuarioLogado.getTipo() == TipoUsuario.ADMIN) {
                    MenuAdministrador menuAdmin = new MenuAdministrador(this);
                    menuAdmin.executar();
                } else {
                    MenuParticipante menuParticipante = new MenuParticipante(this);
                    menuParticipante.executar();
                }
            }
        }
    }

    /**
     * Define o usuário logado no sistema
     */
    public void setUsuarioLogado(Usuario usuario) {
        this.usuarioLogado = usuario;
    }

    /**
     * Retorna o usuário atualmente logado
     */
    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    /**
     * Retorna o scanner para uso nos menus
     */
    public Scanner getScanner() {
        return scanner;
    }
} 