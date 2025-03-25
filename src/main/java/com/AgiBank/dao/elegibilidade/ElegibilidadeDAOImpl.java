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
    public int calcularMesesTrabalhadosAntesDe2019(int idUsuario) {
        LocalDate limite2019 = LocalDate.of(2019, 11, 13);
        int totalMesesTrabalhados = 0;

        String sql = "SELECT periodoInicio, periodoFim FROM Contribuicao WHERE idUsuario = ?";
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
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
    public String[] obterDadosUsuario(int idUsuario) {
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            String sql = "SELECT dataNascimento, genero, profissao FROM Usuario WHERE idUsuario = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idUsuario);
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
    public boolean wasEligibleBefore2019(int idUsuario) {
        String dataNascimento = "";
        String genero = "";
        String profissao = "";

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            String sql = "SELECT dataNascimento, genero, profissao FROM Usuario WHERE idUsuario = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idUsuario);
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
        int mesesTrabalhadosAntes2019 = calcularMesesTrabalhadosAntesDe2019(idUsuario);

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

    @Override
    public int aposentarIdadeMinimaETempo(int idUsuario) {
        String dataNascimento = "";
        String genero = "";
        String profissao = "";

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            String sql = "SELECT dataNascimento, genero, profissao FROM Usuario WHERE idUsuario = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                dataNascimento = rs.getString("dataNascimento");
                genero = rs.getString("genero");
                profissao = rs.getString("profissao");
            } else {
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }

        int idadeEm2019 = calcularIdadeEm2019(dataNascimento, 2019);
        int mesesTrabalhadosAntes2019 = calcularMesesTrabalhadosAntesDe2019(idUsuario);
        int anoAtual = LocalDate.now().getYear();
        int idadeMinima = 0;
        int tempoContribuicaoMinimo = 0;

        switch (profissao.toLowerCase()) {
            case "professor":
                if (genero.equalsIgnoreCase("masculino")) {
                    idadeMinima = 56 + (anoAtual - 2019) / 2;
                    tempoContribuicaoMinimo = 30 * 12;
                } else {
                    idadeMinima = 51 + (anoAtual - 2019) / 2;
                    tempoContribuicaoMinimo = 25 * 12;
                }
                break;

            case "rural":
                if (genero.equalsIgnoreCase("masculino")) {
                    idadeMinima = 61 + (anoAtual - 2019) / 2;
                    tempoContribuicaoMinimo = 35 * 12;
                } else {
                    idadeMinima = 56 + (anoAtual - 2019) / 2;
                    tempoContribuicaoMinimo = 30 * 12;
                }
                break;

            default:
                if (genero.equalsIgnoreCase("masculino")) {
                    idadeMinima = 61 + (anoAtual - 2019) / 2;
                    tempoContribuicaoMinimo = 35 * 12;
                } else {
                    idadeMinima = 56 + (anoAtual - 2019) / 2;
                    tempoContribuicaoMinimo = 30 * 12;
                }
                break;
        }

        if (idadeAtual >= idadeMinima && mesesTrabalhadosAntes2019 >= tempoContribuicaoMinimo) {
            return 1; // Elegível
        } else {
            return 0; // Não elegível
        }
    }

    @Override
    public double calcularValorAposentadoria(int idUsuario) {
        int mesesTrabalhados = calcularMesesTrabalhadosAntesDe2019(idUsuario);
        double mediaContribuicoes = calcularMediaContribuicoes(idUsuario);
        double coeficienteAposentadoria = 0.60 + (0.02 * (mesesTrabalhados / 12 - 30));

        return mediaContribuicoes * coeficienteAposentadoria;
    }

    private double calcularMediaContribuicoes(int idUsuario) {
        double somaSalarios = 0;
        int totalMeses = 0;

        String sql = "SELECT valorSalario FROM Contribuicao WHERE idUsuario = ?";
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                somaSalarios += rs.getDouble("valorSalario");
                totalMeses++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalMeses > 0 ? somaSalarios / totalMeses : 0;
    }
}
