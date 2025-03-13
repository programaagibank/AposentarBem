package com.AgiBank.dao.contribuicao;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;
import java.time.LocalDate;

public class ContribuicaoDAOImpl implements ContribuicaoDAO {
    private final String url;
    private final String username;
    private final String password;

    public ContribuicaoDAOImpl() {
        Dotenv dotenv = Dotenv.load();
        String baseUrl = dotenv.get("DATABASE_URL");
        String port = dotenv.get("DATABASE_PORT");
        this.username = dotenv.get("DATABASE_USERNAME");
        this.password = dotenv.get("DATABASE_PASSWORD");
        this.url = "jdbc:mysql://" + baseUrl + ":" + port + "/aposentarBem";
    }

    public void registrarContribuicao(int idContribuicao, int idUsuario, double valorSalario, LocalDate periodoInicio,
                                      LocalDate periodoFim) throws SQLException {
        if (!usuarioPorId(idUsuario)) {
            throw new SQLException("Usuário com ID " + idUsuario + " não encontrado.");
        }

        if (!periodoContribuicaoEValido(periodoInicio, periodoFim)) {
            throw new SQLException("Período de contribuição inválido.");
        }

        String insertContribuicao = "INSERT INTO Contribuicao (idContribuicao, idUsuario, valorSalario, periodoInicio, periodoFim) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conexao = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = conexao.prepareStatement(insertContribuicao)) {
            ps.setInt(1, idContribuicao);
            ps.setInt(2, idUsuario);
            ps.setDouble(3, valorSalario);
            ps.setDate(4, java.sql.Date.valueOf(periodoInicio));
            ps.setDate(5, java.sql.Date.valueOf(periodoFim));
            ps.executeUpdate();
            System.out.println("Registro realizado com sucesso.");
        } catch (SQLException e) {
            System.out.println("Erro ao registrar contribuição.");
        }
    }

    public void consultarHistorico(int idUsuario) throws SQLException {
        String consulta = "SELECT * FROM Contribuicao WHERE idUsuario = ?";

        try (Connection conexao = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = conexao.prepareStatement(consulta)) {
            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int idContribuicao = rs.getInt("idContribuicao");
                    double valorSalario = rs.getDouble("valorSalario");
                    LocalDate periodoInicioResultado = rs.getDate("periodoInicio").toLocalDate();
                    LocalDate periodoFimResultado = rs.getDate("periodoFim").toLocalDate();

                    System.out.println("ID: " + idContribuicao + ", Valor: " + valorSalario + ", Início: " + periodoInicioResultado + ", Fim: " + periodoFimResultado);
                }
                System.out.println("Consulta do histórico realizada com sucesso.");
            } catch (SQLException e) {
                System.out.println("Erro ao consultar histórico de contribuição.");
            }
        }
    }

    public int obterUltimoIdUsuario() throws SQLException {
        String consulta = "SELECT MAX(idUsuario) AS ultimoId FROM Usuario";

        try (Connection conexao = DriverManager.getConnection(url, username, password);
             Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(consulta)) {
            if (rs.next()) {
                return rs.getInt("ultimoId");
            } else {
                throw new SQLException("Nenhum usuário encontrado.");
            }
        }
    }

    public int obterProximoIdContribuicao() throws SQLException {
        String consulta = "SELECT MAX(idContribuicao) + 1 AS proximoId FROM Contribuicao";

        try (Connection conexao = DriverManager.getConnection(url, username, password);
             Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(consulta)) {
            if (rs.next()) {
                return rs.getInt("proximoId");
            } else {
                return 1; // Caso não haja contribuições, começa com 1
            }
        }
    }

    private boolean periodoContribuicaoEValido(LocalDate periodoInicio, LocalDate periodoFim) {
        return !periodoInicio.isAfter(periodoFim) && !periodoInicio.equals(periodoFim);
    }

    private boolean usuarioPorId(int idUsuario) {
        String consultaUsuario = "SELECT 1 FROM Usuario WHERE idUsuario = ?";

        try (Connection conexao = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = conexao.prepareStatement(consultaUsuario)) {
            ps.setInt(1, idUsuario);
            try (ResultSet resultado = ps.executeQuery()) {
                return resultado.next();
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar por usuário.");
            return false;
        }
    }
}
