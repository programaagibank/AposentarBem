package com.AgiBank.model;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ElegibilidadeAteReforma extends RegrasAposentadoria {

    private List<Contribuicao> contribuicoes;
    private LocalDate DATA_REFERENCIA = LocalDate.of(2019, 11, 13);
    private Usuario usuario;
    private boolean isElegivel = false;

    public ElegibilidadeAteReforma(Usuario usuario, Genero genero, Profissao profissao, int tempoContribuicaoEmMeses, double valorAposentadoria, boolean isElegivel) {
        super(calcularIdadeEm2019(usuario.getDataNascimento()), genero, tempoContribuicaoEmMeses, valorAposentadoria);
        this.usuario = usuario;
    }

    public static int calcularMesesTrabalhados(List<Contribuicao> contribuicoes, LocalDate dataLimite) {
        int totalMeses = 0;

        for (Contribuicao contribuicao : contribuicoes) {
            LocalDate inicio = contribuicao.getPeriodoInicio();
            LocalDate fim = contribuicao.getPeriodoFim().isBefore(dataLimite) ? contribuicao.getPeriodoFim() : dataLimite;

            long meses = ChronoUnit.MONTHS.between(inicio, fim);
            totalMeses += meses;
        }

        return totalMeses;
    }

    public static int calcularIdadeEm2019(LocalDate dataNascimento) {
        Period idade = Period.between(dataNascimento, LocalDate.of(2019, 12, 31));
        return idade.getYears();
    }

    public static boolean isAposentavelPorTempo(Genero genero, Profissao profissao, List<Contribuicao> contribuicoes, LocalDate dataLimite) {
        int totalMeses = calcularMesesTrabalhados(contribuicoes, dataLimite);

        switch (genero) {
            case FEMININO -> {
                if ((profissao == Profissao.GERAL || profissao == Profissao.RURAL) && totalMeses >= 360) {
                    return true;
                } else if (profissao == Profissao.PROFESSOR && totalMeses >= 300) {
                    return true;
                } else {
                    return false;
                }
            }

            case MASCULINO -> {
                if ((profissao == Profissao.GERAL || profissao == Profissao.RURAL) && totalMeses >= 420) {
                    return true;
                } else if (profissao == Profissao.PROFESSOR && totalMeses >= 360) {
                    return true;
                } else {
                    return false;
                }
            }

            default -> {
                return false;
            }
        }
    }

    public static boolean isAposentavelPorIdade(LocalDate dataNascimento, Genero genero, Profissao profissao, List<Contribuicao> contribuicoes, LocalDate dataLimite) {
        int totalMeses = calcularMesesTrabalhados(contribuicoes, dataLimite);
        int idadeEm2019 = calcularIdadeEm2019(dataNascimento);

        switch (genero) {
            case FEMININO -> {
                if ((profissao == Profissao.GERAL || profissao == Profissao.PROFESSOR)
                        && totalMeses >= 180 && idadeEm2019 >= 60) {
                    return true;
                } else if (profissao == Profissao.RURAL && totalMeses >= 180 && idadeEm2019 >= 55) {
                    return true;
                } else {
                    return false;
                }
            }

            case MASCULINO -> {
                if ((profissao == Profissao.GERAL || profissao == Profissao.PROFESSOR)
                        && totalMeses >= 180 && idadeEm2019 >= 65) {
                    return true;
                } else if (profissao == Profissao.RURAL && totalMeses >= 180 && idadeEm2019 >= 60) {
                    return true;
                } else {
                    return false;
                }
            }
            default -> {
                return false;
            }
        }
    }
}
