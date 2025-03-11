package com.AgiBank;

import com.AgiBank.dao.contribuicao.ContribuicaoDAO;
import com.AgiBank.dao.contribuicao.ContribuicaoDAOImpl;
import com.AgiBank.view.ContribuicaoView;

import java.sql.SQLException;
import java.time.format.DateTimeParseException;

public class Main {
    public static void main(String[] args) {
        try {
            // Cria uma instância da View, passando o DAO
            ContribuicaoView contribuicaoView = new ContribuicaoView();

            System.out.println("Teste 1: Registrar Contribuição");
            contribuicaoView.registrarContribuicao();

            System.out.println("\nTeste 2: Consultar Histórico");
            contribuicaoView.consultarHistorico();

            System.out.println("\nTeste 3: Verificar Usuário por ID");
            contribuicaoView.verificarUsuarioPorId();

            System.out.println("\nTeste 4: Verificar Período de Contribuição");
            contribuicaoView.verificarPeriodoContribuicao();

        } catch (DateTimeParseException e) {
            System.out.println("Erro de conversão de data: Formato inválido.");
        } catch (Exception e) {
            System.out.println("Erro inesperado: Ocorreu um erro durante a execução.");
        }
    }
}