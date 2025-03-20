package com.AgiBank.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CalculadoraSalarioBeneficio {

    public static double calcularSalarioBeneficio(List<Double> salarios, List<Integer> meses) {
        if (salarios == null || salarios.isEmpty() || meses == null || meses.isEmpty()) {
            throw new IllegalArgumentException("As listas de salários e meses não podem ser nulas ou vazias.");
        }

        List<Double> todosSalarios = new ArrayList<>();
        for (int i = 0; i < salarios.size(); i++) {
            double salario = salarios.get(i);
            int mesesContribuicao = meses.get(i);
            for (int j = 0; j < mesesContribuicao; j++) {
                todosSalarios.add(salario);
            }
        }

        Collections.sort(todosSalarios);

        int quantidadeDescartar = (int) Math.round(todosSalarios.size() * 0.2);
        List<Double> salariosConsiderados = todosSalarios.subList(quantidadeDescartar, todosSalarios.size());

        double somaSalarios = salariosConsiderados.stream().mapToDouble(Double::doubleValue).sum();

        return somaSalarios / salariosConsiderados.size();
    }
}