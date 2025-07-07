import config.DatabaseConfig;
import view.ConsoleInterface;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("🎓 Sistema de Gerenciamento de Eventos Acadêmicos");
            System.out.println("================================================");
            
            // Inicializar sistema
            System.out.println("🔄 Inicializando sistema...");
            
            // Testar conexão com banco
            try (var conn = DatabaseConfig.getConnection()) {
                System.out.println("✅ Conexão com banco estabelecida com sucesso");
            }
            
            System.out.println("✅ Sistema inicializado com sucesso!\n");
            
            // Iniciar interface de console
            ConsoleInterface interfaceConsole = new ConsoleInterface();
            interfaceConsole.iniciar();
            
        } catch (Exception e) {
            System.err.println("❌ Erro ao inicializar sistema: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}