package com.AgiBank.dao.contribuicao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ContribuicaoDAOImpl {
    private Connection conexao;

    public ContribuicaoDAOImpl(Connection conexao) {
        this.conexao = conexao;
    }

    public void registrarContribuicao(int idUsuario, double valorSalario, LocalDate periodoInicio, LocalDate periodoFim) throws SQLException {
        if (!usuarioPorId(idUsuario)) {
            throw new SQLException("Usuário com ID " + idUsuario + " não encontrado.");
        }

        if (!periodoContribuicaoEValido(idUsuario, periodoInicio, periodoFim)) {
            throw new SQLException("Período de contribuição inválido.");
        }

        String insertContribuicao = "INSERT INTO Contribuicao (idUsuario, valorSalario, periodoInicio, periodoFim) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = conexao.prepareStatement(insertContribuicao)) {
            ps.setInt(1, idUsuario);
            ps.setDouble(2, valorSalario);
            ps.setDate(3, java.sql.Date.valueOf(periodoInicio));
            ps.setDate(4, java.sql.Date.valueOf(periodoFim));
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao registrar contribuição.");
        }
    }

    public void consultarHistorico(int idUsuario) throws SQLException {
        String consulta = "SELECT * FROM Contribuicao WHERE idUsuario = ?";

        try (PreparedStatement ps = conexao.prepareStatement(consulta)) {
            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int idContribuicao = rs.getInt("idContribuicao");
                    double valorSalario = rs.getDouble("valorSalario");
                    LocalDate periodoInicioResultado = rs.getDate("periodoInicio").toLocalDate();
                    LocalDate periodoFimResultado = rs.getDate("periodoFim").toLocalDate();

                    System.out.println("ID: " + idContribuicao + ", Valor: " + valorSalario + ", Início: " + periodoInicioResultado + ", Fim: " + periodoFimResultado);
                }
            } catch (SQLException e) {
                System.out.println("Erro ao consultar histórico de contribuição.");
            }
        }
    }

    private boolean periodoContribuicaoEValido(int idUsuario, LocalDate periodoInicio, LocalDate periodoFim) {
        if (periodoInicio.isAfter(periodoFim) || periodoInicio.equals(periodoFim)) {
            return false;
        }
        return true;
    }

    private boolean usuarioPorId(int idUsuario) {
        String consultaUsuario = "SELECT 1 FROM Usuario WHERE idUsuario = ?";

        try (PreparedStatement ps = conexao.prepareStatement(consultaUsuario)) {
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