package com.AgiBank.controller.contribuicao;

import com.AgiBank.dao.contribuicao.ContribuicaoDAOImpl;
import com.AgiBank.model.CalculadoraSalarioBeneficio;
import com.AgiBank.model.Contribuicao;
import com.AgiBank.view.ContribuicaoView;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class ContribuicaoController {
    private ContribuicaoDAOImpl contribuicaoDAO;
    private ContribuicaoView contribuicaoView;
    private double salarioBeneficio;

    public ContribuicaoController(ContribuicaoDAOImpl contribuicaoDAO, ContribuicaoView contribuicaoView) {
        this.contribuicaoDAO = contribuicaoDAO;
        this.contribuicaoView = contribuicaoView;
    }

    public void processarContribuicoes(int idUsuario) {
        try {
            List<Double> salarios = new ArrayList<>();
            List<Integer> mesesContribuicao = new ArrayList<>();

            List<Contribuicao> contribuicoes = contribuicaoDAO.obterSalariosPorUsuario(idUsuario);
            for (Contribuicao contribuicao : contribuicoes) {
                salarios.add(contribuicao.getValorSalario());

                LocalDate inicio = contribuicao.getPeriodoInicio();
                LocalDate fim = contribuicao.getPeriodoFim();
                Period periodo = Period.between(inicio, fim);

                int meses = periodo.getYears() * 12 + periodo.getMonths();
                if (periodo.getDays() > 0) {
                    meses += 1;
                }
                mesesContribuicao.add(meses);
            }

            if (salarios.isEmpty() || mesesContribuicao.isEmpty()) {
                throw new IllegalArgumentException("As listas de salários e meses não podem ser nulas ou vazias.");
            }

            this.salarioBeneficio = CalculadoraSalarioBeneficio.calcularSalarioBeneficio(salarios, mesesContribuicao);
            contribuicaoView.exibirSalarioBeneficio(this.salarioBeneficio);

        } catch (SQLException e) {
            System.err.println("Erro ao obter contribuições do banco de dados: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public double getSalarioBeneficio() {
        return salarioBeneficio;
    }
}