package com.AgiBank;

import com.AgiBank.view.ContribuicaoView;

public class Main {
    public static void main(String[] args) {
        ContribuicaoView contribuicaoView = new ContribuicaoView();
        contribuicaoView.registrarContribuicao();
        contribuicaoView.consultarHistorico();
    }
}