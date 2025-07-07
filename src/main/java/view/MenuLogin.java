package view;

import dao.UsuarioDAO;
import enums.PerfilParticipante;
import enums.TipoUsuario;
import model.Administrador;
import model.Participante;
import model.Usuario;
import services.AutenticacaoService;

/**
 * Menu responsável pelo login e cadastro de usuários
 */
public class MenuLogin extends Menu {
    private UsuarioDAO usuarioDAO;
    private AutenticacaoService autenticacaoService;

    public MenuLogin(ConsoleInterface consoleInterface) {
        super(consoleInterface);
        this.usuarioDAO = new UsuarioDAO();
        this.autenticacaoService = new AutenticacaoService(usuarioDAO);
    }

    @Override
    protected void exibirMenu() {
        exibirCabecalho("🔐 MENU DE LOGIN");
        System.out.println("1. Fazer login");
        System.out.println("2. Cadastrar novo usuário");
        System.out.println("3. Sair");
        System.out.print("Escolha uma opção: ");
    }

    @Override
    protected void processarOpcao(int opcao) {
        switch (opcao) {
            case 1:
                fazerLogin();
                break;
            case 2:
                cadastrarUsuario();
                break;
            case 3:
                exibirInfo("Obrigado por usar o sistema!");
                System.exit(0);
            default:
                exibirErro("Opção inválida!");
        }
    }

    private void fazerLogin() {
        exibirCabecalho("🔐 LOGIN");
        System.out.print("Email: ");
        String email = lerString();
        System.out.print("Senha: ");
        String senha = lerString();

        try {
            Usuario usuario = autenticacaoService.autenticar(email, senha);
            consoleInterface.setUsuarioLogado(usuario);
            exibirSucesso("Login realizado com sucesso!");
        } catch (Exception e) {
            exibirErro("Erro no login: " + e.getMessage());
        }
    }

    private void cadastrarUsuario() {
        exibirCabecalho("📝 CADASTRO DE USUÁRIO");
        System.out.print("Nome: ");
        String nome = lerString();
        System.out.print("Email: ");
        String email = lerString();
        System.out.print("Senha: ");
        String senha = lerString();
        System.out.print("Tipo (1-Administrador, 2-Participante): ");
        int tipoOpcao = lerInteiro();

        try {
            Usuario novoUsuario = criarUsuario(nome, email, senha, tipoOpcao);
            usuarioDAO.inserir(novoUsuario);
            exibirSucesso("Usuário cadastrado com sucesso!");
        } catch (Exception e) {
            exibirErro("Erro ao cadastrar: " + e.getMessage());
        }
    }

    private Usuario criarUsuario(String nome, String email, String senha, int tipoOpcao) {
        if (tipoOpcao == 1) {
            return new Administrador(nome, email, senha);
        } else if (tipoOpcao == 2) {
            System.out.print("Perfil (1-Aluno, 2-Professor, 3-Profissional): ");
            int perfilOpcao = lerInteiro();
            PerfilParticipante perfil = obterPerfil(perfilOpcao);
            return new Participante(nome, email, senha, perfil);
        } else {
            throw new IllegalArgumentException("Tipo inválido!");
        }
    }

    private PerfilParticipante obterPerfil(int perfilOpcao) {
        return switch (perfilOpcao) {
            case 1 -> PerfilParticipante.ALUNO;
            case 2 -> PerfilParticipante.PROFESSOR;
            case 3 -> PerfilParticipante.PROFISSIONAL;
            default -> PerfilParticipante.ALUNO;
        };
    }
} 