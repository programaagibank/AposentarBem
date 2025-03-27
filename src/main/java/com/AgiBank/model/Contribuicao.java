package com.AgiBank.model;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public class Contribuicao {
    private int idContribuicao;
    private int idUsuario;
    private double valorSalario;
    private LocalDate periodoInicio;
    private LocalDate periodoFim;

    public Contribuicao(int idUsuario, double valorSalario, LocalDate periodoInicio, LocalDate periodoFim) {
        this.idUsuario = idUsuario;
        this.valorSalario = valorSalario;
        this.periodoInicio = periodoInicio;
        this.periodoFim = periodoFim;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public double getValorSalario() {
        return valorSalario;
    }

    public void setValorSalario(double valorSalario) {
        this.valorSalario = valorSalario;
    }

    public LocalDate getPeriodoInicio() {
        return periodoInicio;
    }

    public void setPeriodoInicio(LocalDate periodoInicio) {
        this.periodoInicio = periodoInicio;
    }

    public LocalDate getPeriodoFim() {
        return periodoFim;
    }

    public void setPeriodoFim(LocalDate periodoFim) {
        this.periodoFim = periodoFim;
    }

    public int getIdContribuicao() {
        return idContribuicao;
    }

    public void setIdContribuicao(int idContribuicao) {
        this.idContribuicao = idContribuicao;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public static int calcularAnosContribuidos(List<Contribuicao> contribuicoes) {
        return calcularTotalMesesContribuidos(contribuicoes) / 12;
    }

    public static int calcularMesesRestantes(List<Contribuicao> contribuicoes) {
        return calcularTotalMesesContribuidos(contribuicoes) % 12;
    }

    public static double calcularSalarioTotal(List<Contribuicao> contribuicoes) {
        double salarioTotal = 0;
        for (Contribuicao contribuicao : contribuicoes) {
            int meses = Period.between(contribuicao.getPeriodoInicio(), contribuicao.getPeriodoFim())
                    .getYears() * 12 + Period.between(contribuicao.getPeriodoInicio(), contribuicao.getPeriodoFim()).getMonths();
            salarioTotal += contribuicao.getValorSalario() * meses;
        }
        return salarioTotal;
    }

    public static int calcularTotalMesesContribuidos(List<Contribuicao> contribuicoes) {
        int totalMeses = 0;
        for (Contribuicao contribuicao : contribuicoes) {
            Period periodo = Period.between(contribuicao.getPeriodoInicio(), contribuicao.getPeriodoFim());
            totalMeses += periodo.getYears() * 12 + periodo.getMonths();
        }
        return totalMeses;
    }
}