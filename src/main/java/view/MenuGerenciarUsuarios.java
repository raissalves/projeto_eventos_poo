package view;

import dao.UsuarioDAO;
import model.Usuario;
import java.util.List;

/**
 * Menu para gerenciar usuários
 */
public class MenuGerenciarUsuarios extends Menu {
    private UsuarioDAO usuarioDAO;

    public MenuGerenciarUsuarios(ConsoleInterface consoleInterface) {
        super(consoleInterface);
        this.usuarioDAO = new UsuarioDAO();
    }

    @Override
    protected void exibirMenu() {
        exibirCabecalho("👥 GERENCIAR USUÁRIOS");
        System.out.println("1. Listar usuários");
        System.out.println("2. Buscar usuário por email");
        System.out.println("3. Editar usuário");
        System.out.println("4. Excluir usuário");
        exibirRodape();
    }

    @Override
    protected void processarOpcao(int opcao) {
        switch (opcao) {
            case 1:
                listarUsuarios();
                break;
            case 2:
                buscarUsuarioPorEmail();
                break;
            case 3:
                editarUsuario();
                break;
            case 4:
                excluirUsuario();
                break;
            case 0:
                return;
            default:
                exibirErro("Opção inválida!");
        }
    }

    private void listarUsuarios() {
        exibirCabecalho("👥 LISTA DE USUÁRIOS");
        try {
            List<Usuario> usuarios = usuarioDAO.listarTodos();
            if (usuarios.isEmpty()) {
                exibirInfo("Nenhum usuário encontrado.");
            } else {
                for (Usuario usuario : usuarios) {
                    System.out.println("ID: " + usuario.getId());
                    System.out.println("Nome: " + usuario.getNome());
                    System.out.println("Email: " + usuario.getEmail());
                    System.out.println("Tipo: " + usuario.getTipo());
                    if (usuario.getPerfil() != null) {
                        System.out.println("Perfil: " + usuario.getPerfil());
                    }
                    System.out.println("---");
                }
            }
        } catch (Exception e) {
            exibirErro("Erro ao listar usuários: " + e.getMessage());
        }
    }

    private void buscarUsuarioPorEmail() {
        exibirAviso("Funcionalidade em desenvolvimento...");
    }

    private void editarUsuario() {
        exibirAviso("Funcionalidade em desenvolvimento...");
    }

    private void excluirUsuario() {
        exibirAviso("Funcionalidade em desenvolvimento...");
    }
} 