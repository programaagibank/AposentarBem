package com.AgiBank.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Pedagios extends RegrasAposentadoria {
    private List<Contribuicao> contribuicoes;
    private LocalDate DATA_REFERENCIA = LocalDate.of(1994, 7, 1);
    private Profissao profissao;

    public Pedagios(List<Contribuicao> contribuicoes, int idade, Genero genero, Profissao profissao) {
        super(idade, genero, contribuicoes.size() * 12, 0); // Initialize the superclass with total months
        this.contribuicoes = filtrarContribuicoesValidas(contribuicoes);
        this.profissao = profissao;
        this.setTempoContribuicaoEmMeses(Contribuicao.calcularAnosContribuidos(this.contribuicoes) * 12);
    }

    private List<Contribuicao> filtrarContribuicoesValidas(List<Contribuicao> contribuicoes) {
        List<Contribuicao> contribuicoesValidas = new ArrayList<>();
        for (Contribuicao contribuicao : contribuicoes) {
            if (!contribuicao.getPeriodoInicio().isBefore(DATA_REFERENCIA)) {
                contribuicoesValidas.add(contribuicao);
            }
        }
        return contribuicoesValidas;
    }

    private double calcularSomaSalarios() {
        return Contribuicao.calcularSalarioTotal(contribuicoes);
    }

    private double calcularMediaSalarial() {
        int totalMeses = getTempoContribuicaoEmMeses();
        if (totalMeses <= 0) {
            return 0;
        }
        return calcularSomaSalarios() / totalMeses;
    }

    public boolean isElegivelPedagio50() {
        int totalMeses = getTempoContribuicaoEmMeses();
        int tempoComPedagio = totalMeses + (totalMeses / 2);
        return tempoComPedagio >= 24;
    }

    public boolean isElegivelPedagio100() {
        int idade = getIdade();
        int tempoContribuicaoEmMeses = getTempoContribuicaoEmMeses();

        if (getGenero() == Genero.MASCULINO) {
            if (profissao == Profissao.GERAL) {
                return idade >= 60 && tempoContribuicaoEmMeses < 420;
            } else if (profissao == Profissao.PROFESSOR) {
                return idade >= 55 && tempoContribuicaoEmMeses < 360;
            }
        } else if (getGenero() == Genero.FEMININO) {
            if (profissao == Profissao.GERAL) {
                return idade >= 57 && tempoContribuicaoEmMeses < 360;
            } else if (profissao == Profissao.PROFESSOR) {
                return idade >= 52 && tempoContribuicaoEmMeses < 300;
            }
        }

        return false;
    }


    public double calcularPedagio50() {
        double media = calcularMediaSalarial();
        FatorPrevidenciario fp = new FatorPrevidenciario(contribuicoes);
        return media * fp.calcularFatorPrevidenciario();
    }

    public double calcularPedagio100() {
        return calcularMediaSalarial();
    }

    public double getBeneficio() {
        return calcularMediaSalarial();
    }
}