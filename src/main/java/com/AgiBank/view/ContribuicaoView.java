package com.AgiBank.view;

import com.AgiBank.controller.contribuicao.ContribuicaoController;
import com.AgiBank.dao.contribuicao.ContribuicaoDAO;
import com.AgiBank.model.CalculadoraSalarioBeneficio;
import com.AgiBank.model.Contribuicao;
import com.AgiBank.model.ContribuicaoTotais;
import com.AgiBank.model.Usuario;

import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ContribuicaoView {
    private final Scanner input = new Scanner(System.in);
    private final ContribuicaoDAO contribuicaoDAO;
    private final Usuario usuario;
    private final ContribuicaoController contribuicaoController;
    private double salarioBeneficio;


    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final NumberFormat CURRENCY_FORMATTER = NumberFormat.getCurrencyInstance();

    public ContribuicaoView(ContribuicaoDAO contribuicaoDAO, Usuario usuario, ContribuicaoController contribuicaoController) {
        this.contribuicaoDAO = contribuicaoDAO;
        this.usuario = usuario;
        this.contribuicaoController = contribuicaoController;
    }

    public void registrarContribuicao(int idUsuario) {
        try {
            System.out.println("Vamos coletar os registros das suas contribuições para calcular seu tempo de contribuição e salário total.");

            int contadorRegistros = 1;
            boolean adicionarMais = true;

            while (adicionarMais) {
                System.out.println("\n--- " + contadorRegistros + "º Registro de Contribuição ---");
                System.out.println("Informe os dados deste período de contribuição (formal ou informal).");

                LocalDate periodoInicio;
                while (true) {
                    periodoInicio = coletarData("Data de início (formato dd/MM/yyyy): ");

                    // Verifica sobreposição com registros existentes
                    if (verificarSobreposicao(periodoInicio, periodoInicio, idUsuario)) {
                        System.out.println("\nErro: Este período de contribuição se sobrepõe a um registro já existente. " +
                                "Não é permitido ter dois registro de contribuições no mesmo período.\n");
                    } else {
                        break;
                    }
                }

                LocalDate dataCorte = LocalDate.of(1994, 7, 1);
                if (periodoInicio.isBefore(dataCorte)) {
                    System.out.println("\nAtenção: Contribuições antes de julho de 1994 serão registradas,\n" +
                            "mas não contarão no cálculo do valor da aposentadoria, apenas no tempo de contribuição.\n" +
                            "Isso porque foi quando o Brasil adotou o Real como sua moeda oficial.\n");
                }

                double salario = coletarSalario("Valor do salário ou renda mensal recebida R$: ");
                LocalDate periodoFim;

                boolean ajustesFeitos = false;
                while (true) {
                    String mensagem = ajustesFeitos
                            ? "Houve outro reajuste no seu salário durante esse período? (s/n): "
                            : "Houve algum reajuste no seu salário durante esse período?\n" +
                            "(Ex: Aumento de salário, nova fonte de renda) (s/n): ";

                    if (!solicitarConfirmacao(mensagem)) {
                        break;
                    }

                    ajustesFeitos = true;
                    double novoSalario = coletarSalario("Informe o novo valor do salário ou renda R$: ");
                    LocalDate dataAjuste;

                    while (true) {
                        dataAjuste = coletarData("Data em que o reajuste ocorreu (formato dd/MM/yyyy): ");
                        if (dataAjuste.isBefore(periodoInicio)) {
                            System.out.println("\nErro: A data do reajuste não pode ser anterior ao início do período de contribuição.");
                        } else {
                            break;
                        }
                    }

                    if (verificarSobreposicao(periodoInicio, dataAjuste.minusDays(1), idUsuario)) {
                        System.out.println("\nErro: Este período de contribuição se sobrepõe a um registro já existente. " +
                                "Não é permitido ter duas contribuições no mesmo período.\n");
                        continue;
                    }

                    registrarContribuicaoNoBanco(idUsuario, salario, periodoInicio, dataAjuste.minusDays(1));
                    periodoInicio = dataAjuste;
                    salario = novoSalario;
                }

                if (solicitarConfirmacao("Este é seu emprego atual / você ainda está contribuindo dessa forma? (s/n): ")) {
                    periodoFim = LocalDate.now();
                    adicionarMais = false;
                } else {
                    periodoFim = coletarData("Data de término deste período de contribuição (formato dd/MM/yyyy): ");
                }

                if (verificarSobreposicao(periodoInicio, periodoFim, idUsuario)) {
                    System.out.println("\nErro: Este período de contribuição se sobrepõe a um registro já existente. " +
                            "Não é permitido ter duas contribuições no mesmo período.");
                    continue;
                }
                registrarContribuicaoNoBanco(idUsuario, salario, periodoInicio, periodoFim);
                if (!adicionarMais) {
                    break;
                }
                if (!solicitarConfirmacao("\nDeseja adicionar outro registro de contribuição? (s/n): ")) {
                    adicionarMais = false; // Encerra o loop
                }

                contadorRegistros++;
            }

            System.out.println("\nContribuições registradas com sucesso!");
        } catch (SQLException e) {
            System.out.println("\nErro ao registrar contribuição: " + e.getMessage());
        }
    }

    private boolean verificarSobreposicao(LocalDate inicioNovo, LocalDate fimNovo, int idUsuario) throws SQLException {
        List<Contribuicao> contribuicoes = contribuicaoDAO.consultarHistorico(idUsuario);
        for (Contribuicao c : contribuicoes) {
            LocalDate inicioExistente = c.getPeriodoInicio();
            LocalDate fimExistente = c.getPeriodoFim();

            if (inicioNovo.isBefore(fimExistente) && fimNovo.isAfter(inicioExistente)) {
                return true;
            }
        }
        return false;
    }

    private void registrarContribuicaoNoBanco(int idUsuario, double salario, LocalDate inicio, LocalDate fim) throws SQLException {
        int idContribuicao = contribuicaoDAO.obterProximoIdContribuicao();
        Contribuicao contribuicao = new Contribuicao(idUsuario, salario, inicio, fim);
        contribuicaoDAO.registrarContribuicao(
                idContribuicao,
                contribuicao.getIdUsuario(),
                contribuicao.getValorSalario(),
                contribuicao.getPeriodoInicio(),
                contribuicao.getPeriodoFim()
        );
    }

    private LocalDate coletarData(String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem);
                String data = input.nextLine();
                LocalDate dataFormatada = LocalDate.parse(data, DATE_FORMATTER);
                validarDataContribuicao(dataFormatada);
                return dataFormatada;
            } catch (DateTimeParseException e) {
                System.out.println("Formato de data inválido! Por favor, use o formato dd/MM/yyyy.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private double coletarSalario(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                return Double.parseDouble(input.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Por favor, insira um valor válido para o salário.");
            }
        }
    }

    private boolean solicitarConfirmacao(String mensagem) {
        System.out.print(mensagem);
        String resposta = input.nextLine();
        return resposta.equalsIgnoreCase("s");
    }

    private void validarDataContribuicao(LocalDate data) {
        LocalDate dataAtual = LocalDate.now();
        LocalDate dataNascimento = usuario.getDataNascimento();
        LocalDate dataMinimaContribuicao = dataNascimento.plusYears(15);

        if (data.isBefore(dataNascimento)) {
            throw new IllegalArgumentException("A data de contribuição não pode ser anterior à sua data de nascimento.");
        }
        if (data.isBefore(dataMinimaContribuicao)) {
            throw new IllegalArgumentException("A contribuição só pode começar após atingir 15 anos de idade.");
        }
        if (data.isAfter(dataAtual)) {
            throw new IllegalArgumentException("A data de contribuição não pode ser futura.");
        }
    }

    private String formatarSalario(double salario) {
        return CURRENCY_FORMATTER.format(salario);
    }

    private String formatarData(LocalDate data) {
        return data.format(DATE_FORMATTER);
    }

    private String formatarTempoContribuicao(LocalDate inicio, LocalDate fim) {
        Period periodo = Period.between(inicio, fim);
        return String.format("%d anos e %d meses", periodo.getYears(), periodo.getMonths());
    }

 public ContribuicaoTotais calcularTotais(int idUsuario) {
    try {
        List<Contribuicao> contribuicoes = contribuicaoDAO.consultarHistorico(idUsuario);

        if (contribuicoes.isEmpty()) {
            System.out.println("Nenhuma contribuição encontrada para o usuário.");
            return new ContribuicaoTotais(0, 0, 0);
        }

        double salarioTotal = 0;
        int totalMesesContribuicao = 0;
        LocalDate dataLimite = LocalDate.of(1994, 7, 1);
        for (Contribuicao c : contribuicoes) {
            LocalDate inicio = c.getPeriodoInicio();
            LocalDate fim = c.getPeriodoFim();

            if (inicio.isAfter(fim)) {
                throw new IllegalArgumentException("A data de início não pode ser posterior à data de fim.");
            }

            Period periodoContribuicao = Period.between(inicio, fim);
            int mesesContribuicao = periodoContribuicao.getYears() * 12 + periodoContribuicao.getMonths();
            if (periodoContribuicao.getDays() > 0) {
                mesesContribuicao += 1;
            }
            totalMesesContribuicao += mesesContribuicao;

            if (!fim.isBefore(dataLimite)) {
                LocalDate inicioValido = inicio.isBefore(dataLimite) ? dataLimite : inicio;

                Period periodoSalario = Period.between(inicioValido, fim);
                int mesesSalario = periodoSalario.getYears() * 12 + periodoSalario.getMonths();
                if (periodoSalario.getDays() > 0) {
                    mesesSalario += 1;
                }

                salarioTotal += c.getValorSalario() * mesesSalario;
            }
        }
        int totalAnos = totalMesesContribuicao / 12;

        int totalMesesValidos = (int) (salarioTotal > 0 ? salarioTotal / contribuicoes.stream()
                .filter(c -> !c.getPeriodoFim().isBefore(dataLimite))
                .mapToDouble(c -> c.getValorSalario())
                .average()
                .orElse(0) : 0);
        return new ContribuicaoTotais(totalAnos, totalMesesContribuicao, salarioTotal);

    } catch (SQLException e) {
        System.out.println("Erro ao calcular totais: " + e.getMessage());
        return new ContribuicaoTotais(0, 0, 0);
    }
}
    public void consultarHistorico(int idUsuario) {
        try {
            List<Contribuicao> contribuicoes = contribuicaoDAO.consultarHistorico(idUsuario);
            System.out.println("\n---- Histórico de Contribuições ----");

            List<Double> salarios = new ArrayList<>();
            List<Integer> mesesContribuicao = new ArrayList<>();

            for (Contribuicao c : contribuicoes) {
                System.out.println("Salário: " + formatarSalario(c.getValorSalario()) +
                        ", Início: " + formatarData(c.getPeriodoInicio()) +
                        ", Fim: " + formatarData(c.getPeriodoFim()) +
                        ", Tempo de Contribuição: " + formatarTempoContribuicao(c.getPeriodoInicio(), c.getPeriodoFim()));
                System.out.println("----------------------------------------");

                salarios.add(c.getValorSalario());

                LocalDate inicio = c.getPeriodoInicio();
                LocalDate fim = c.getPeriodoFim();
                Period periodo = Period.between(inicio, fim);
                int meses = periodo.getYears() * 12 + periodo.getMonths();
                if (periodo.getDays() > 0) {
                    meses += 1; // Considera dias como 1 mês
                }
                mesesContribuicao.add(meses);
            }

            ContribuicaoTotais totais = calcularTotais(idUsuario);

            int anosContribuicao = totais.getAnosContribuicao();
            int totalMesesContribuicao = totais.getMesesContribuicao();
            double salarioTotal = totais.getSalarioTotal();

            System.out.println("\nResumo das Contribuições:");
            System.out.println("Anos de Contribuição: " + anosContribuicao);
            System.out.println("Meses de Contribuição: " + totalMesesContribuicao);
            System.out.println("Salário Total adquirido: " + formatarSalario(salarioTotal));

            if (salarios.isEmpty() || mesesContribuicao.isEmpty()) {
                throw new IllegalArgumentException("As listas de salários e meses não podem ser nulas ou vazias.");
            }
            //regra antiga
            exibirSalarioBeneficio(CalculadoraSalarioBeneficio.calcularSalarioBeneficio(salarios, mesesContribuicao));

        } catch (SQLException e) {
            System.out.println("Erro ao consultar histórico: " + e.getMessage());
        }
    }

    //mostrar esse metodo na regra antiga, quando requisitado
    public void exibirSalarioBeneficio(double salarioBeneficio) {
        System.out.printf("O salário de benefício é: R$ %.2f%n", salarioBeneficio);

    }
}
