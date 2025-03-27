package com.AgiBank.dao.elegibilidade;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class ElegibilidadeDAOImpl implements ElegibilidadeDAO {
    private final String url;
    private final String username;
    private final String password;

    public ElegibilidadeDAOImpl() {
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
            }
        }
        return -1;
    }

    @Override
    public int calcularMesesTrabalhadosAntesDe2019(int userId) {
        LocalDate limite2019 = LocalDate.of(2019, 11, 13);
        int totalMesesTrabalhados = 0;

        String sql = "SELECT periodoInicio, periodoFim FROM Contribuicao WHERE idUsuario = ?";
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Date inicio = rs.getDate("periodoInicio");
                Date fim = rs.getDate("periodoFim");

                if (inicio != null && fim != null) {
                    LocalDate dataInicio = inicio.toLocalDate();
                    LocalDate dataFim = fim.toLocalDate();

                    if (dataFim.isBefore(limite2019)) {
                        totalMesesTrabalhados += (int) ChronoUnit.MONTHS.between(dataInicio, dataFim);
                    } else if (dataInicio.isBefore(limite2019)) {
                        totalMesesTrabalhados += (int) ChronoUnit.MONTHS.between(dataInicio, limite2019);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalMesesTrabalhados;
    }

    @Override
    public String[] obterDadosUsuario(int userId) {
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            String sql = "SELECT dataNascimento, genero, profissao FROM Usuario WHERE idUsuario = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new String[]{rs.getString("dataNascimento"), rs.getString("genero"), rs.getString("profissao")};
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int calcularIdadeEm2019(String dataNascimento, int anoReferencia) {
        if (dataNascimento == null || dataNascimento.isEmpty()) {
            throw new IllegalArgumentException("Data de nascimento inválida.");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dataNasc = LocalDate.parse(dataNascimento, formatter);

        return Period.between(dataNasc, LocalDate.of(anoReferencia, 1, 1)).getYears();
    }


    @Override
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

        int idadeEm2019 = calcularIdadeEm2019(dataNascimento, 2019);
        int mesesTrabalhadosAntes2019 = calcularMesesTrabalhadosAntesDe2019(userId);
        //Verificar meses
         System.out.println("Meses trabalhados" + mesesTrabalhadosAntes2019);

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
}
