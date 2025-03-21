package com.AgiBank.model;

public class Pontos {
    public static void main(String[] args) {
        String sexo = "Masculino";
        String categoria = "Professor";
        int idade = 0;
        int tempoContribuicao = 0;
        int pontos = idade + tempoContribuicao;
        int tempoContribuicaoMeses = tempoContribuicao * 12;
        int pontosNecessarios = calcularPontosNecessarios(sexo);
        int tempoMinimoContribuicao = 0;
        int anosExtras = calcularAnosExtras(tempoContribuicao, tempoMinimoContribuicao);
        double coefiAposen = 0.60 + (0.02 * anosExtras);
        // double[] salarios = salarios;
        double somaSalarios = 0;
        double mediaContri = somaSalarios / tempoContribuicaoMeses;
        double aposentadoria = mediaContri * coefiAposen;

        if(categoria == "Professor"){
            tempoMinimoContribuicao = (sexo == "Masculino") ? 30 : 25;
        }else{
            tempoMinimoContribuicao = (sexo == "Masculino") ? 35 : 30;
        }

        if (tempoContribuicao >= tempoMinimoContribuicao && pontos >= pontosNecessarios) {

        } else {

        }

    }

    private static int calcularPontosNecessarios(String sexo) {
        int anoAtual = java.time.Year.now().getValue();
        int inicioProgressao = 2019;
        int incrementoAnual = anoAtual - inicioProgressao;

        if (sexo == "Masculino") {
            return Math.min(105, 96 + incrementoAnual);
        } else {
            return Math.min(100, 86 + incrementoAnual);
        }
    }

    private static int calcularAnosExtras(int tempoContribuicao, int tempoMinimo) {
        return Math.max(0, tempoContribuicao - tempoMinimo);
    }
}


