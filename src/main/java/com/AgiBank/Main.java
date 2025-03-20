package com.AgiBank;

import com.AgiBank.model.ContribuicaoTotais;
import com.AgiBank.model.FatorPrevidenciario;

public class Main {
    public static void main(String[] args) {
        ContribuicaoTotais contribuicaoTotais = new ContribuicaoTotais(30, 6, 50000.0);
        FatorPrevidenciario fp = new FatorPrevidenciario(contribuicaoTotais);

        System.out.println("Fator Previdenciário: " + fp.calcularFatorPrevidenciario());

//        ContribuicaoTotais contribuicaoTotais = new ContribuicaoTotais(30, 6, 50000.0); // 30 anos, 6 meses, salário de 50.000
//        FatorPrevidenciario fp = new FatorPrevidenciario(contribuicaoTotais);
//        System.out.println("Fator Previdenciário: " + fp.calcularFatorPrevidenciario());
    }
}
