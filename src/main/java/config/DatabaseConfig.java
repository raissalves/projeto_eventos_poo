package config;

import java.sql.*;
import java.util.Properties;
import java.io.InputStream;
import exceptions.DatabaseException;

public class DatabaseConfig {
    private static boolean tabelasCriadas = false;
    private static String dbUrl;

    static {
        try {
            carregarConfiguracoes();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar configurações do banco: " + e.getMessage(), e);
        }
    }

    private static void carregarConfiguracoes() throws Exception {
        InputStream input = null;
        try {
            input = DatabaseConfig.class.getClassLoader().getResourceAsStream("database.properties");
            if (input == null) {
                throw new DatabaseException("Arquivo database.properties não encontrado");
            }
            
            Properties prop = new Properties();
            prop.load(input);
            dbUrl = prop.getProperty("db.url");
            
            if (dbUrl == null || dbUrl.trim().isEmpty()) {
                throw new DatabaseException("URL do banco não configurada no database.properties");
            }
        } finally {
            if (input != null) {
                input.close();
            }
        }
    }

    public static Connection getConnection() {
        try {
            Connection conexao = DriverManager.getConnection(dbUrl);
            
            // Criar tabelas apenas na primeira conexão
            if (!tabelasCriadas) {
                criarTabelas(conexao);
                tabelasCriadas = true;
            }
            
            return conexao;
        } catch (Exception e) {
            throw new DatabaseException("Erro na conexão com o banco: " + e.getMessage(), e);
        }
    }

    private static void criarTabelas(Connection conexao) {
        InputStream input = null;
        try {
            input = DatabaseConfig.class.getClassLoader().getResourceAsStream("schema.sql");
            if (input == null) {
                throw new DatabaseException("Arquivo schema.sql não encontrado");
            }
            
            String sql = new String(input.readAllBytes());
            try (Statement stmt = conexao.createStatement()) {
                stmt.executeUpdate(sql);
            }
        } catch (Exception e) {
            throw new DatabaseException("Erro ao criar tabelas: " + e.getMessage(), e);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (Exception e) {
                    // Ignorar erro ao fechar input
                }
            }
        }
    }

    public static void closeConnection(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao fechar conexão: " + e.getMessage(), e);
        }
    }
}