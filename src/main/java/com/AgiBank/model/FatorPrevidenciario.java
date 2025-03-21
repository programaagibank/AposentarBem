package com.AgiBank.model;

public class FatorPrevidenciario {
    private int anosContribuidos;
    private double EXPECTATIVA_DE_VIDA = 76.4;
    private double ALIQUOTA = 0.31;

    public FatorPrevidenciario(ContribuicaoTotais contribuicaoTotais) {
        this.anosContribuidos = contribuicaoTotais.getAnosContribuicao();
    }
    public double calcularFatorPrevidenciario() {
        return (anosContribuidos * ALIQUOTA) / (EXPECTATIVA_DE_VIDA * 100);
    }
}