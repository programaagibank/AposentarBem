package com.AgiBank.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RegrasAposentadoriaTest {
    @Mock
    private Contribuicao contribuicao1;
    @Mock
    private Contribuicao contribuicao2;
    @Mock
    private Contribuicao contribuicao3;

    private RegrasAposentadoria regrasMasculino;
    private RegrasAposentadoria regrasFeminino;
    private List<Contribuicao> contribuicoes;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        regrasMasculino = new RegrasAposentadoria(65, RegrasAposentadoria.Genero.MASCULINO, 240, 0); // 240 meses = 20 anos
        regrasFeminino = new RegrasAposentadoria(62, RegrasAposentadoria.Genero.FEMININO, 180, 0); // 180 meses = 15 anos
        contribuicoes = new ArrayList<>();

        // Mock contributions
        mockContribuicao(contribuicao1, 3000.0, LocalDate.of(2015, 1, 1), LocalDate.of(2017, 12, 31));
        mockContribuicao(contribuicao2, 4000.0, LocalDate.of(2018, 1, 1), LocalDate.of(2020, 12, 31));
        mockContribuicao(contribuicao3, 5000.0, LocalDate.of(2021, 1, 1), LocalDate.of(2023, 12, 31));

        contribuicoes.addAll(List.of(contribuicao1, contribuicao2, contribuicao3));
    }

    private void mockContribuicao(Contribuicao contribuicao, double salario, LocalDate inicio, LocalDate fim) {
        when(contribuicao.getValorSalario()).thenReturn(salario);
        when(contribuicao.getPeriodoInicio()).thenReturn(inicio);
        when(contribuicao.getPeriodoFim()).thenReturn(fim);
    }

    private void testCalcularMediaContribuicoes(RegrasAposentadoria regras) {
        // Setup
        double expectedSalarioTotal = 432000.0; // (3000 * 36) + (4000 * 36) + (5000 * 36)
        int expectedMeses = 108; // 9 anos * 12 meses

        try (MockedStatic<Contribuicao> mockedStatic = mockStatic(Contribuicao.class)) {
            mockedStatic.when(() -> Contribuicao.calcularSalarioTotal(contribuicoes)).thenReturn(expectedSalarioTotal);
            mockedStatic.when(() -> Contribuicao.calcularAnosContribuidos(contribuicoes)).thenReturn(9);
            mockedStatic.when(() -> Contribuicao.calcularMesesRestantes(contribuicoes)).thenReturn(0);

            // Execute
            double media = regras.calcularMediaContribuicoes(contribuicoes);

            // Verify
            assertEquals(4000.0, media, 0.01);
            mockedStatic.verify(() -> Contribuicao.calcularSalarioTotal(contribuicoes));
            mockedStatic.verify(() -> Contribuicao.calcularAnosContribuidos(contribuicoes));
            mockedStatic.verify(() -> Contribuicao.calcularMesesRestantes(contribuicoes));
        }
    }

    @Test
    void testCalcularMediaContribuicoesMasculino() {
        testCalcularMediaContribuicoes(regrasMasculino);
    }

    @Test
    void testCalcularMediaContribuicoesFeminino() {
        testCalcularMediaContribuicoes(regrasFeminino);
    }

    @Test
    void testCalcularCoeficienteAposentadoriaMasculino() {
        double coeficiente = regrasMasculino.calcularCoeficienteAposentadoria();
        assertEquals(0.60, coeficiente, 0.01);
    }

    @Test
    void testCalcularCoeficienteAposentadoriaFeminino() {
        double coeficiente = regrasFeminino.calcularCoeficienteAposentadoria();
        assertEquals(0.60, coeficiente, 0.01);
    }

    @Test
    void testCalcularValorAposentadoriaMasculino() {
        testCalcularValorAposentadoria(regrasMasculino);
    }

    @Test
    void testCalcularValorAposentadoriaFeminino() {
        testCalcularValorAposentadoria(regrasFeminino);
    }

    private void testCalcularValorAposentadoria(RegrasAposentadoria regras) {
        try (MockedStatic<Contribuicao> mockedStatic = mockStatic(Contribuicao.class)) {
            // Setup
            mockedStatic.when(() -> Contribuicao.calcularSalarioTotal(contribuicoes)).thenReturn(432000.0);
            mockedStatic.when(() -> Contribuicao.calcularAnosContribuidos(contribuicoes)).thenReturn(9);
            mockedStatic.when(() -> Contribuicao.calcularMesesRestantes(contribuicoes)).thenReturn(0);

            // Execute
            double valor = regras.calcularValorAposentadoria(contribuicoes);

            // Verify
            assertEquals(2400.0, valor, 0.01);
        }
    }
}
