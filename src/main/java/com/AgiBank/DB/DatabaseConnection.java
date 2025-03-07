package com.AgiBank.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import io.github.cdimascio.dotenv.Dotenv;

public class DatabaseConnection {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();

        String port = dotenv.get("DATABASE_PORT");
        String baseUrl = dotenv.get("DATABASE_URL");
        String username = dotenv.get("DATABASE_USERNAME");
        String password = dotenv.get("DATABASE_PASSWORD");

        String url = "jdbc:mysql://" + baseUrl + ":" + port + "/aposentarBem";

        if (url == null || username == null || password == null) {
            System.out.println(url);
            System.out.println(username);
            System.out.println(password);
            System.err.println("One or more environment variables are missing.");
            return;
        }

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("Connected to the database!");

            createTables(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTables(Connection connection) throws SQLException {
        String createUsersTable = "CREATE TABLE IF NOT EXISTS Usuario (" +
                "idUsuario INT AUTO_INCREMENT PRIMARY KEY," +
                "nome VARCHAR(255) NOT NULL," +
                "dataNascimento DATE NOT NULL," +
                "genero ENUM('MASCULINO', 'FEMININO') NOT NULL," +
                "profissao ENUM('GERAL', 'PROFESSOR', 'TRABALHADOR_RURAL') NOT NULL," +
                "idadeAposentadoriaDesejada INT" +
                ")";

        String createContribuicoesTable = "CREATE TABLE IF NOT EXISTS Contribuicao (" +
                "idContribuicao INT AUTO_INCREMENT PRIMARY KEY," +
                "idUsuario INT," +
                "valorSalario DECIMAL(10, 2) NOT NULL," +
                "periodoInicio DATE NOT NULL," +
                "periodoFim DATE NOT NULL," +
                "CONSTRAINT contribuicao_ibfk_1 FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario)" +
                ")";

        String createElegibilidadeTable = "CREATE TABLE IF NOT EXISTS Elegibilidade (" +
                "idRegra INT AUTO_INCREMENT PRIMARY KEY," +
                "nomeRegra ENUM('TEMPO', 'IDADE', 'PEDAGIO_50', 'PEDAGIO_100', 'PONTOS', 'PROFESSOR', 'TRABALHADOR_RURAL')," +
                "tempoMinimoContribuicao INT," +
                "idadeMinima INT," +
                "pedagio50 BOOLEAN," +
                "pedagio100 BOOLEAN," +
                "pontuacaoMinima INT," +
                "tetoMinimoINSS DECIMAL(10, 2)," +
                "tetoMaximoINSS DECIMAL(10, 2)" +
                ")";

        String createAposentadoriaTable = "CREATE TABLE IF NOT EXISTS Aposentadoria (" +
                "idResultado INT AUTO_INCREMENT PRIMARY KEY," +
                "idUsuario INT," +
                "idadeAtual INT," +
                "tempoTotalContribuicao INT," +
                "mediaSalarial DECIMAL(10, 2)," +
                "idadeMinimaNecessaria INT," +
                "dataElegivelAposentadoria DATE," +
                "valorEstimado DECIMAL(10, 2)," +
                "idRegra INT," +
                "FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario)," +
                "FOREIGN KEY (idRegra) REFERENCES Elegibilidade(idRegra)" +
                ")";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createUsersTable);
            System.out.println("Usuario table created successfully!");

            stmt.execute(createContribuicoesTable);
            System.out.println("Contribuicao table created successfully!");

            stmt.execute(createElegibilidadeTable);
            System.out.println("Elegibilidade table created successfully!");

            stmt.execute(createAposentadoriaTable);
            System.out.println("Aposentadoria table created successfully!");
        }
    }

}
