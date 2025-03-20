package com.AgiBank.model;

public class ContribuicaoTotais {
    private final int anosContribuicao;
    private final int mesesContribuicao;
    private final double salarioTotal;

    public ContribuicaoTotais(int anosContribuicao, int mesesContribuicao, double salarioTotal) {
        this.anosContribuicao = anosContribuicao;
        this.mesesContribuicao = mesesContribuicao;
        this.salarioTotal = salarioTotal;
    }

    public int getAnosContribuicao() {
        return anosContribuicao;
    }

    public int getMesesContribuicao() {
        return mesesContribuicao;
    }

    public double getSalarioTotal() {
        return salarioTotal;
    }
}
