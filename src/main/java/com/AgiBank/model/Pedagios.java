package com.AgiBank.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Pedagios {
    private int idade;
    private int totalAnos;
    private int totalMeses;
    private double media;
    private double beneficio;
    private List<Contribuicao> contribuicoes;
    private LocalDate DATA_REFERENCIA = LocalDate.of(1994, 7, 1);

    public Pedagios(List<Contribuicao> contribuicoes, int idade) {
        this.idade = idade;
        this.contribuicoes = filtrarContribuicoesValidas(contribuicoes);
        this.totalAnos = Contribuicao.calcularAnosContribuidos(this.contribuicoes);
        this.totalMeses = this.totalAnos * 12;
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
        if (totalMeses <= 0) {
            return media = 0;
        }
        return media = calcularSomaSalarios() / totalMeses;
    }

    public boolean isElegivelPedagio50() {
        int tempoComPedagio = totalMeses + (totalMeses / 2);
        return tempoComPedagio >= 24;
    }

    public boolean isElegivelPedagio100() {
        if (idade >= 60 && totalMeses < 420) {
            return true;
        }
        if (idade >= 57 && totalMeses < 360) {
            return true;
        }

        if (idade >= 55 && totalMeses < 360) {
            return true;
        }
        if (idade >= 52 && totalMeses < 300) {
            return true;
        }
        return false;
    }

    public double calcularPedagio50() {
        calcularMediaSalarial();

        FatorPrevidenciario fp = new FatorPrevidenciario(contribuicoes);
        beneficio = media * fp.calcularFatorPrevidenciario();

        return beneficio;
    }

    public double calcularPedagio100() {
        calcularMediaSalarial();
        return beneficio = media;
    }

    public double getBeneficio() {
        return beneficio;
    }
}