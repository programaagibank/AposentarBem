package com.AgiBank.model;

import java.util.List;
public class FatorPrevidenciario {
    private int anosContribuidos;
    private double EXPECTATIVA_DE_VIDA = 76.4;
    private double ALIQUOTA = 0.31;

    public FatorPrevidenciario(List<Contribuicao> contribuicoes) {
        this.anosContribuidos = Contribuicao.calcularAnosContribuidos(contribuicoes);
    }
    public double calcularFatorPrevidenciario() {
        return (anosContribuidos * ALIQUOTA) / (EXPECTATIVA_DE_VIDA * 100);
    }
}