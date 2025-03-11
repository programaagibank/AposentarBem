package com.AgiBank.view;

import com.AgiBank.dao.contribuicao.ContribuicaoDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class ContribuicaoView {
    public Scanner input = new Scanner(System.in);
    public ContribuicaoDAO contribuicaoDAO;

    public void registrarContribuicao() {
        try {
            double valorSalario;

            System.out.print("Digite o ID do usuário: ");
            int idUsuario = input.nextInt();

            do {
                System.out.print("Digite o valor do salário: ");
                valorSalario = input.nextDouble();

                System.out.print("Deseja inserir outro valor de salário? (s/n): ");
                String resposta = input.next();
                if (resposta.equals("n")) {
                    break;
                }
            } while (true);

            System.out.print("Digite a data de início: ");
            String dataInicio = input.next();
            LocalDate periodoInicio = LocalDate.parse(dataInicio, DateTimeFormatter.ofPattern("dd-MM-yyyy"));

            System.out.print("Digite a data de fim: ");
            String dataFinal = input.next();
            LocalDate periodoFim = LocalDate.parse(dataFinal, DateTimeFormatter.ofPattern("dd-MM-yyyy"));

            contribuicaoDAO.registrarContribuicao(idUsuario, valorSalario, periodoInicio, periodoFim);
            System.out.println("Contribuição registrada com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao registrar contribuição.");
        } finally {
            input.close();
        }
    }

    public void consultarHistorico() {
        try {
            System.out.print("Digite o ID do usuário: ");
            int idUsuario = input.nextInt();
            contribuicaoDAO.consultarHistorico(idUsuario);
        } catch (SQLException e) {
            System.out.println("Erro ao consultar histórico.");
        }
    }

    public void verificarUsuarioPorId() {
        try {
            System.out.print("Digite o ID do usuário: ");
            int idUsuario = input.nextInt();
            boolean usuarioExiste = contribuicaoDAO.usuarioPorId(idUsuario);
            System.out.println("Usuário existe: " + usuarioExiste);
        } catch (SQLException e) {
            System.out.println("Erro ao verificar usuário por ID.");
        }
    }

    public void verificarPeriodoContribuicao() {
        try {
            System.out.print("Digite o ID do usuário: ");
            int idUsuario = input.nextInt();

            System.out.print("Digite a data de início: ");
            String dataInicioStr = input.next();
            LocalDate periodoInicio = LocalDate.parse(dataInicioStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));

            System.out.print("Digite a data de fim: ");
            String dataFimStr = input.next();
            LocalDate periodoFim = LocalDate.parse(dataFimStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));

            boolean periodoValido = contribuicaoDAO.periodoContribuicaoEValido(idUsuario, periodoInicio, periodoFim);
            System.out.println("Período válido: " + periodoValido);
        } catch (DateTimeParseException e) {
            System.out.println("Erro ao converter data: Formato inválido. Use dd-MM-yyyy.");
        } catch (Exception e) {
            System.out.println("Erro inesperado ao verificar período.");
        }
    }
}