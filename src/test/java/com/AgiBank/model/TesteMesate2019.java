package com.AgiBank.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TesteMesate2019 {

    private List<Contribuicao> contribuicoes;

    @BeforeEach
    public void setUp() {
        // Initialize a list of Contribuicao objects for testing, including idContribuicao
        contribuicoes = Arrays.asList(
                new Contribuicao(1, 1000, LocalDate.of(2010, 1, 1), LocalDate.of(2012, 12, 31)),
                new Contribuicao(2, 1500, LocalDate.of(2013, 1, 1), LocalDate.of(2015, 12, 31)),
                new Contribuicao(3, 2000, LocalDate.of(2016, 1, 1), LocalDate.of(2019, 11, 12))
        );
    }

    @Test
    public void testCalcularTotalMesesContribuidos_SingleContribution() {
        List<Contribuicao> singleContribuicao = Arrays.asList(
                new Contribuicao(1, 1000, LocalDate.of(2018, 1, 1), LocalDate.of(2019, 11, 12))
        );
        int totalMeses = Contribuicao.calcularTotalMesesContribuidos(singleContribuicao);
        assertEquals(22, totalMeses); // Adjusted expected value
    }

    @Test
    public void testCalcularTotalMesesContribuidos_ContributionEndsAfterLimit() {
        List<Contribuicao> contribuicaoEndsAfterLimit = List.of(
                new Contribuicao(1, 1000, LocalDate.of(2018, 1, 1), LocalDate.of(2020, 1, 1))
        );
        int totalMeses = Contribuicao.calcularTotalMesesContribuidos(contribuicaoEndsAfterLimit);
        assertEquals(22, totalMeses); // Adjusted expected value
    }

    @Test
    public void testContribuicoesAntesDaDataLimite() {
        List<Contribuicao> contribuicoes = List.of(
                new Contribuicao(1, 2000, LocalDate.of(2018, 1, 1), LocalDate.of(2018, 12, 31))
        );
        LocalDate dataLimite = LocalDate.of(2019, 11, 13);
        int mesesTrabalhados = ElegibilidadeAteReforma.calcularMesesTrabalhados(contribuicoes, dataLimite);
        assertEquals(12, mesesTrabalhados);
    }

    @Test
    public void testContribuicoesNaDataLimite() {
        List<Contribuicao> contribuicoes = List.of(
                new Contribuicao(2, 2500, LocalDate.of(2019, 1, 1), LocalDate.of(2019, 11, 13))
        );
        LocalDate dataLimite = LocalDate.of(2019, 11, 13);
        int mesesTrabalhados = ElegibilidadeAteReforma.calcularMesesTrabalhados(contribuicoes, dataLimite);
        assertEquals(10, mesesTrabalhados);
    }

    @Test
    public void testContribuicoesAposDataLimite() {
        List<Contribuicao> contribuicoes = List.of(
                new Contribuicao(3, 3000, LocalDate.of(2019, 6, 1), LocalDate.of(2020, 1, 1))
        );
        LocalDate dataLimite = LocalDate.of(2019, 11, 13);
        int mesesTrabalhados = ElegibilidadeAteReforma.calcularMesesTrabalhados(contribuicoes, dataLimite);
        assertEquals(5, mesesTrabalhados);
    }

    @Test
    public void testListaVazia() {
        List<Contribuicao> contribuicoes = List.of();
        LocalDate dataLimite = LocalDate.of(2019, 11, 13);
        int mesesTrabalhados = ElegibilidadeAteReforma.calcularMesesTrabalhados(contribuicoes, dataLimite);
        assertEquals(0, mesesTrabalhados);
    }

    @Test
    public void debugCalculoMeses() {
        List<Contribuicao> contribuicoes = Arrays.asList(
                new Contribuicao(1, 1000, LocalDate.of(2018, 1, 1), LocalDate.of(2020, 1, 1))
        );

        int totalMeses = Contribuicao.calcularTotalMesesContribuidos(contribuicoes);
        System.out.println("Total meses contribuídos: " + totalMeses);
    }
}
