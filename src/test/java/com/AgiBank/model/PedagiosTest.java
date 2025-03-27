package com.AgiBank.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PedagiosTest {
    private Contribuicao contribuicao1;
    private Contribuicao contribuicao2;
    private Contribuicao contribuicao3;
    private Pedagios pedagios;

    @BeforeEach
    void setUp() {
        contribuicao1 = new Contribuicao(1, 2000.0, LocalDate.of(2000, 1, 1), LocalDate.of(2005, 12, 31));
        contribuicao2 = new Contribuicao(1, 2200.0, LocalDate.of(2006, 1, 1), LocalDate.of(2010, 12, 31));
        contribuicao3 = new Contribuicao(1, 2500.0, LocalDate.of(2011, 1, 1), LocalDate.of(2018, 12, 31));
    }

    @Test
    void testCalcularPedagio50Geral() {
        pedagios = new Pedagios(Arrays.asList(contribuicao1, contribuicao2), 55, RegrasAposentadoria.Genero.MASCULINO, RegrasAposentadoria.Profissao.GERAL);
        double beneficio = pedagios.calcularPedagio50();
        assertTrue(beneficio > 0, "O benefício do Pedágio 50 para profissões gerais deve ser maior que zero.");
    }

    @Test
    void testCalcularPedagio100Professor() {
        pedagios = new Pedagios(Arrays.asList(contribuicao1, contribuicao2), 55, RegrasAposentadoria.Genero.FEMININO, RegrasAposentadoria.Profissao.PROFESSOR);
        double beneficio = pedagios.calcularPedagio100();
        assertTrue(beneficio > 0, "O benefício do Pedágio 100 para professores deve ser maior que zero.");
    }
}