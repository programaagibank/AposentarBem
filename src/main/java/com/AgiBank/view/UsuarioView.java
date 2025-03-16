package com.AgiBank.view;

import com.AgiBank.model.Usuario;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;

public class UsuarioView {

    private final Scanner scanner = new Scanner(System.in);

    public Usuario coletarDadosUsuario() {
        String nome = coletarNome();
        String dataNascimento = coletarDataNascimento();
        String genero = coletarGenero();
        String profissao = coletarProfissao();
        int idade = calcularIdade(dataNascimento);
        int idadeAposentadoriaDesejada = coletarIdadeAposentadoriaDesejada();

        return new Usuario(nome, dataNascimento, genero, profissao,idadeAposentadoriaDesejada);
    }

    private String coletarNome() {
        while (true) {
            System.out.print("Digite seu nome: ");
            String nome = scanner.nextLine().trim();
            if (Usuario.validarNome(nome)) {
                return nome;
            } else {
                System.out.println("Nome inválido. Tente novamente.");
            }
        }
    }

    private String coletarDataNascimento() {

        while (true) {
            try {
                System.out.print("Digite sua data de nascimento (DD/MM/AAAA): ");
                String dataNascimento = scanner.nextLine();
                if (Usuario.validarDataNascimento(dataNascimento)) {
                    return dataNascimento;
                } else {
                    System.out.println("Data inválida. Tente novamente.");
                }
            } catch (Exception e) {
                System.out.println("Data inválida. Por favor, use o formato DD/MM/AAAA.");
            }
        }
    }

    private int calcularIdade(String dataNascimento) {
        LocalDate dataNasc = LocalDate.parse(dataNascimento, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        return Period.between(dataNasc, LocalDate.now()).getYears();
    }

    private String coletarGenero() {
        while (true) {
            System.out.print("Qual seu gênero? (Masculino/Feminino): ");
            String genero = scanner.nextLine().trim();
            if (Usuario.validarGenero(genero)) {
                return genero;
            } else {
                System.out.println("Gênero inválido. Tente novamente.");
            }
        }
    }

    private String coletarProfissao() {
        String[] opcoesValidas = {"Geral", "Professor", "Rural"};

        while (true) {
            System.out.print("Qual sua profissão? (Geral, Professor, Rural): ");
            String profissao = scanner.nextLine().trim();
            profissao = profissao.substring(0, 1).toUpperCase() + profissao.substring(1).toLowerCase();
            if (Usuario.validarProfissao(profissao)) {
                return profissao;
            } else {
                System.out.println("Profissão inválida. Tente novamente.");
            }
        }
    }

    private int coletarIdadeAposentadoriaDesejada() {
        while (true) {
            try {
                System.out.print("Digite a idade desejada para aposentadoria: ");
                int idadeAposentadoria = scanner.nextInt();
                scanner.nextLine();
                if (Usuario.validarIdadeAposentadoria(idadeAposentadoria)) {
                    return idadeAposentadoria;
                } else {
                    System.out.println("Idade de aposentadoria inválida. Tente novamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida! Digite apenas números inteiros.");
                scanner.next();
            }
        }
    }

    public void exibirMensagem(String mensagem) {
        System.out.println(mensagem);
    }
}
