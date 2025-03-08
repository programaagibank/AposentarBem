package com.AgiBank;

import com.AgiBank.DB.ContribuicaoDAO;
import com.AgiBank.DB.DatabaseConnection;
import com.AgiBank.model.Contribuicao;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        try {
            System.out.print("Digite o ID do usuário: ");
            int idUsuario = input.nextInt();

            System.out.print("Digite o valor do salário: ");
            double valorSalario = input.nextDouble();

            System.out.print("Digite a data de início (dd-mm-aa): ");
            String dataInicio = input.next();
            LocalDate periodoInicio = LocalDate.parse(dataInicio, DateTimeFormatter.ofPattern("dd-MM-yyyy"));

            System.out.print("Digite a data de fim (dd-mm-aa): ");
            String dataFinal = input.next();
            LocalDate periodoFim = LocalDate.parse(dataFinal, DateTimeFormatter.ofPattern("dd-MM-yyyy"));

            // fixme - arrumar conexão com o banco
            Connection conexao = DatabaseConnection.getConnection();
            ContribuicaoDAO contribuicaoDAO = new ContribuicaoDAO(conexao);
            Contribuicao contribuicao = new Contribuicao(idUsuario, valorSalario, periodoInicio, periodoFim);
            contribuicaoDAO.registrarContribuicao(contribuicao);

            System.out.println("Contribuição registrada com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao registrar contribuição.");
        } finally {
            input.close();
        }
    }
}