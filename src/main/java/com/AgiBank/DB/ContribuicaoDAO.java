package com.AgiBank.DB;

import com.AgiBank.model.Contribuicao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ContribuicaoDAO {
    private Connection conexao;

    public ContribuicaoDAO(Connection conexao) {
        this.conexao = conexao;
    }

    public void registrarContribuicao(Contribuicao contribuicao) {
        String insertContribuicao = "INSERT INTO Contribuicao (idUsuario, valorSalario, periodoInicio, periodoFim) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = conexao.prepareStatement(insertContribuicao)) {
            ps.setInt(1, contribuicao.getIdUsuario());
            ps.setDouble(2, contribuicao.getValorSalario());
            ps.setDate(3, java.sql.Date.valueOf(contribuicao.getPeriodoInicio()));
            ps.setDate(4, java.sql.Date.valueOf(contribuicao.getPeriodoFim()));
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao registrar contribuição.");
        }
    }

    public void consultarHistorico(int idUsuario) {
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
            }
        } catch (SQLException e) {
            System.out.println("Erro ao consultar histórico de contribuição.");
        }
    }
}
