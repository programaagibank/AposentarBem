package com.AgiBank.model;

import com.AgiBank.dao.usuario.UsuarioDAOImpl;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Usuario {
    private String nome;
    private String dataNascimento;
    private String genero;
    private String profissao;
    private int idadeAposentadoriaDesejada;
    private String[] opcoesValidas = {"Geral", "Professor", "Rural"};
    private int idade;

    public Usuario(String nome, String dataNascimento, String genero, String profissao, int idadeAposentadoriaDesejada) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.genero = genero;
        this.profissao = profissao;
        this.idadeAposentadoriaDesejada = idadeAposentadoriaDesejada;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getProfissao() {
        return profissao;
    }

    public void setProfissao(String profissao) {
        this.profissao = profissao;
    }

    public int getIdadeAposentadoriaDesejada() {
        return idadeAposentadoriaDesejada;
    }

    public void setIdadeAposentadoriaDesejada(int idadeAposentadoriaDesejada) {
        this.idadeAposentadoriaDesejada = idadeAposentadoriaDesejada;
    }

    public static Usuario coletarDadosUsuario(String nome, String dataNascimento, String genero, String profissao, int idadeAposentadoriaDesejada, String[] opcoesValidas, int idade) {

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Digite seu nome: ");
            nome = scanner.nextLine().trim();

            if (nome.isEmpty()) {
                System.out.println("O nome não pode estar vazio. Tente novamente.");
            } else if (!nome.matches("[a-zA-ZÀ-ÿ^~\\s]+")) {
                System.out.println("O nome não pode conter números ou caracteres especiais inválidos. Tente novamente.");
            } else {
                break;
            }
        }

        LocalDate dataAniversario = null;
        LocalDate diaAtual = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        while (dataAniversario == null) {
            System.out.print("Digite sua data de nascimento (DD/MM/AAAA): ");
            dataNascimento = scanner.nextLine();
            try {
                dataAniversario = LocalDate.parse(dataNascimento, formatter);

                if (dataAniversario.isAfter(diaAtual)) {
                    System.out.println("Data de nascimento não pode ser maior que a data de hoje.");
                    dataAniversario = null;
                    continue;
                }

                idade = Period.between(dataAniversario, diaAtual).getYears();
                if (idade < 15) {
                    System.out.println("Você precisa ter pelo menos 15 anos.");
                    dataAniversario = null;
                }

            } catch (Exception e) {
                System.out.println("Data inválida. Por favor, use o formato DD/MM/AAAA.");
            }
        }

        System.out.print("Qual seu gênero?\n");
        System.out.println("Masculino ou Feminino");
        genero = scanner.nextLine();

        while (!genero.equalsIgnoreCase("Masculino") && !genero.equalsIgnoreCase("Feminino") && !genero.equalsIgnoreCase("masculino") && !genero.equalsIgnoreCase("feminino")) {
            System.out.println("Opção inválida. Digite novamente (Masculino ou Feminino): ");
            genero = scanner.nextLine();
        }

        genero = genero.substring(0, 1).toUpperCase() + genero.substring(1).toLowerCase();


        while (true) {
            System.out.println("Dentro dessas opções de trabalho, qual se encaixa a você? (Geral, Professor, Rural)");
            profissao = scanner.nextLine().trim();

            profissao = profissao.substring(0, 1).toUpperCase() + profissao.substring(1).toLowerCase();

            if (profissao.matches("[a-zA-Z]+") && isOpcaoValida(profissao, opcoesValidas)) {
                break;
            } else {
                System.out.println("Entrada inválida! Digite uma das opções corretamente (Geral, Professor, Rural).");
            }
        }

        try {
            boolean retiradaValidaIdade = false;
            while (!retiradaValidaIdade) {

                System.out.print("Digite a idade que deseja se aposentar?\n ");

                if (genero.equalsIgnoreCase("Masculino")) {
                    System.out.println("A idade minima para sua aposentadoria é de 65 anos");
                    idadeAposentadoriaDesejada = scanner.nextInt();
                    if (idadeAposentadoriaDesejada >= 65) {
                        retiradaValidaIdade = true;
                    } else {
                        System.out.println("A idade mínima para aposentadoria é de 65 anos. Tente novamente.\n");
                    }
                } else {
                    System.out.println("A idade minima para sua aposentadoria é de 62 anos");
                    idadeAposentadoriaDesejada = scanner.nextInt();
                    if (idadeAposentadoriaDesejada >= 62) {
                        retiradaValidaIdade = true;
                    } else {
                        System.out.println("A idade mínima para aposentadoria é de 62 anos. Tente novamente.");
                    }
                }
                if (idadeAposentadoriaDesejada >= 90) {
                    System.out.println("Idade muito alta, tente um valor menor que 90 anos");
                    idadeAposentadoriaDesejada = scanner.nextInt();
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida! Digite apenas números inteiros.");
            scanner.next();
        }

        Usuario usuario = new Usuario(nome, dataNascimento, genero, profissao, idadeAposentadoriaDesejada);

        com.AgiBank.dao.usuario.UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
        usuarioDAO.criarUsuario(usuario);

        scanner.close();
        return usuario;
    }

    private static boolean isOpcaoValida (String input, String[]opcoes){
        for (String opcao : opcoes) {
            if (input.equals(opcao)) {
                return true;
            }
        }
        return false;
    }
}
