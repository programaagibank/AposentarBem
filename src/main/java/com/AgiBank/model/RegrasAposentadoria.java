package com.AgiBank.model;

public class RegrasAposentadoria {
    private int anosContribuicao;
    private int idadeMinimaHomem;
    private int idadeMinimaMulher;

    public RegrasAposentadoria(ContribuicaoTotais contribuicaoTotais) {
        this.anosContribuicao = contribuicaoTotais.getAnosContribuicao();
    }

    public int getAnosContribuicao() {
        return anosContribuicao;
    }

    public int getIdadeMinimaHomem() {
        return idadeMinimaHomem;
    }

    public void setIdadeMinimaHomem(int idadeMinimaHomem) {
        this.idadeMinimaHomem = idadeMinimaHomem;
    }

    public int getIdadeMinimaMulher() {
        return idadeMinimaMulher;
    }

    public void setIdadeMinimaMulher(int idadeMinimaMulher) {
        this.idadeMinimaMulher = idadeMinimaMulher;
    }

    public double calcularCoeficienteAposentadoria(int anosMinimos) {
        int anosExtras = Math.max(0, anosContribuicao - anosMinimos);
        return 0.60 + (0.02 * anosExtras);
    }

    public double calcularMediaContribuicoes(ContribuicaoTotais contribuicaoTotais) {
        if (contribuicaoTotais.getMesesContribuicao() == 0) {
            return 0;
        }
        return contribuicaoTotais.getSalarioTotal() / contribuicaoTotais.getMesesContribuicao();
    }

    public double calcularValorAposentadoria(ContribuicaoTotais contribuicaoTotais, int anosMinimos) {
        double coeficiente = calcularCoeficienteAposentadoria(anosMinimos);
        double mediaContribuicoes = calcularMediaContribuicoes(contribuicaoTotais);
        return mediaContribuicoes * coeficiente;
    }
}
