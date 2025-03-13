package com.AgiBank.view;

import com.AgiBank.dao.contribuicao.ContribuicaoDAO;
import com.AgiBank.dao.contribuicao.ContribuicaoDAOImpl;
import com.AgiBank.model.Contribuicao;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class ContribuicaoView {
    private final Scanner input = new Scanner(System.in);
    private final ContribuicaoDAO contribuicaoDAO = new ContribuicaoDAOImpl();

    public void registrarContribuicao() {
        try {
            // Obter o último idUsuario criado
            int idUsuario = contribuicaoDAO.obterUltimoIdUsuario();
            System.out.println("Agora, vamos colocar suas contribuições!");

            do {
                int idContribuicao = contribuicaoDAO.obterProximoIdContribuicao();
                LocalDate periodoInicio = coletarData("início");
                LocalDate periodoFim = coletarData("fim");

                System.out.print("Digite o valor do salário: ");
                double salario = input.nextDouble();

                Contribuicao contribuicao = new Contribuicao(idUsuario, salario, periodoInicio,
                        periodoFim);
                contribuicaoDAO.registrarContribuicao(
                        idContribuicao,
                        contribuicao.getIdUsuario(),
                        contribuicao.getValorSalario(),
                        contribuicao.getPeriodoInicio(),
                        contribuicao.getPeriodoFim()
                );

                System.out.print("Deseja inserir outro valor de salário? (s/n): ");
                String resposta = input.next();

                if (resposta.equalsIgnoreCase("n")) {
                    break;
                }
            } while (true);

            System.out.println("Contribuições registradas com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao registrar contribuição.");
        }
    }

    private LocalDate coletarData(String tipo) {
        String data;
        LocalDate dataFormatada = null;

        while (dataFormatada == null) {
            System.out.print("Digite a data de " + tipo + " (dd/mm/aaaa): ");
            data = input.next();

            try {
                dataFormatada = LocalDate.parse(data, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (Exception e) {
                System.out.println("Data inválida! Tente novamente.");
            }
        }
        return dataFormatada;
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
}
