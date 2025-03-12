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
        if (!idContribuicaoExiste(idContribuicao)) {
            throw new SQLException("O ID da contribuição não existe no banco de dados.");
        }

        if (!usuarioPorId(idUsuario)) {
            throw new SQLException("Usuário com ID " + idUsuario + " não encontrado.");
        }

        if (!verificarContribuicaoAssociadaAUsuario(idContribuicao, idUsuario)) {
            throw new SQLException("O ID da contribuição não está associado ao usuário com ID " + idUsuario + ".");
        }

        if (!periodoContribuicaoEValido(periodoInicio, periodoFim)) {
            throw new SQLException("Período de contribuição inválido.");
        }

        String insertContribuicao = "INSERT INTO Contribuicao (idContribuicao, idUsuario, valorSalario, periodoInicio, periodoFim) " +  "VALUES (?, ?, ?, ?, ?)";

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

    private boolean periodoContribuicaoEValido(LocalDate periodoInicio, LocalDate periodoFim) {
        if (periodoInicio.isAfter(periodoFim) || periodoInicio.equals(periodoFim)) {
            return false;
        }
        return true;
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

    private boolean idContribuicaoExiste(int idContribuicao) throws SQLException {
        String query = "SELECT 1 FROM Contribuicao WHERE idContribuicao = ?";

        try (Connection conexao = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = conexao.prepareStatement(query)) {
            ps.setInt(1, idContribuicao);
            try (ResultSet resultado = ps.executeQuery()) {
                if (resultado.next()) {
                    return resultado.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    private boolean verificarContribuicaoAssociadaAUsuario(int idContribuicao, int idUsuario) {
        String query = "SELECT 1 FROM Contribuicao WHERE idContribuicao = ? AND idUsuario = ?";

        try (Connection conexao = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = conexao.prepareStatement(query)) {
            ps.setInt(1, idContribuicao);
            ps.setInt(2, idUsuario);
            try (ResultSet resultado = ps.executeQuery()) {
                return resultado.next();
            }
        } catch (SQLException e) {
            System.out.println("Erro ao verificar associação de contribuição com usuário.");
            return false;
        }
    }
}