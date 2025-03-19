package com.AgiBank.dao.elegibilidade;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class ElegibilidadeImpl  implements ElegibilidadeDAO{

    private final String url;
    private final String username;
    private final String password;

    public ElegibilidadeImpl() {
        Dotenv dotenv = Dotenv.load();
        String port = dotenv.get("DATABASE_PORT");
        String baseUrl = dotenv.get("DATABASE_URL");
        this.username = dotenv.get("DATABASE_USERNAME");
        this.password = dotenv.get("DATABASE_PASSWORD");

        this.url = "jdbc:mysql://" + baseUrl + ":" + port + "/aposentarBem";
    }
@Override
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

    public int calcularMesesTrabalhadosAntesDe2019(int userId) {
        LocalDate primeiraContribuicao = null;
        LocalDate ultimaContribuicao = null;

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            String sql = "SELECT MIN(periodoInicio) AS primeira, MAX(periodoFim) AS ultima FROM Contribuicao WHERE idUsuario = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Date primeira = rs.getDate("primeira");
                Date ultima = rs.getDate("ultima");
                if (primeira != null) primeiraContribuicao = primeira.toLocalDate();
                if (ultima != null) ultimaContribuicao = ultima.toLocalDate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (primeiraContribuicao == null || ultimaContribuicao == null) {
            return 0;
        }

        LocalDate limite2019 = LocalDate.of(2019, 11, 13);
        int mesesTrabalhados;

        if (ultimaContribuicao.isBefore(limite2019)) {
            mesesTrabalhados = (int) ChronoUnit.MONTHS.between(primeiraContribuicao, ultimaContribuicao);
        } else {
            mesesTrabalhados = (int) ChronoUnit.MONTHS.between(primeiraContribuicao, limite2019);
        }

        System.out.println("Meses trabalhados antes de 2019: " + mesesTrabalhados);
        return mesesTrabalhados;
    }



    public int calcularIdade(String dataNascimento, int anoReferencia) {
        LocalDate dataNasc = LocalDate.parse(dataNascimento, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return Period.between(dataNasc, LocalDate.of(anoReferencia, 1, 1)).getYears();
    }

    public boolean wasEligibleBefore2019(int userId) {
        String dataNascimento = "";
        String genero = "";
        String profissao = "";

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            String sql = "SELECT dataNascimento, genero, profissao FROM Usuario WHERE idUsuario = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                dataNascimento = rs.getString("dataNascimento");
                genero = rs.getString("genero");
                profissao = rs.getString("profissao");
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        int idadeEm2019 = calcularIdade(dataNascimento, 2019);
        int mesesTrabalhadosAntes2019 = calcularMesesTrabalhadosAntesDe2019(userId);

        switch (profissao.toLowerCase()) {
            case "professor":
                boolean professorApto = (genero.equalsIgnoreCase("masculino") && mesesTrabalhadosAntes2019 >= 360) ||
                        (genero.equalsIgnoreCase("feminino") && mesesTrabalhadosAntes2019 >= 300);
                return professorApto;

            case "rural":
                boolean ruralApto = (genero.equalsIgnoreCase("masculino") && idadeEm2019 >= 60 && mesesTrabalhadosAntes2019 >= 180) ||
                        (genero.equalsIgnoreCase("feminino") && idadeEm2019 >= 55 && mesesTrabalhadosAntes2019 >= 180);
                return ruralApto;

            default:
                boolean aposentadoriaPorIdade = (idadeEm2019 >= 65 && mesesTrabalhadosAntes2019 >= 180);
                boolean aposentadoriaPorTempo = (mesesTrabalhadosAntes2019 >= 420);
                return aposentadoriaPorIdade || aposentadoriaPorTempo;
        }
    }

    public void verificarUltimoUsuario() throws SQLException {
        int ultimoId = obterUltimoIdUsuario();
        if (ultimoId == -1) {
            System.out.println("Nenhum usuário encontrado no banco.");
            return;
        }

        boolean elegivel = wasEligibleBefore2019(ultimoId);

        if (elegivel) {
            System.out.println("O último usuário cadastrado já podia se aposentar antes de 2019.");
        } else {
            System.out.println("O último usuário cadastrado NÃO podia se aposentar antes de 2019.");
        }
    }
}
