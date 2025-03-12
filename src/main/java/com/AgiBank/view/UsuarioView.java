package com.AgiBank.view;

import com.AgiBank.model.Usuario;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;

public class UsuarioView {
    public static Usuario colectUserDataFromConsole(Scanner scanner) {

        String nome;
        String dataNascimento = "";
        String genero;
        String profissao;
        int idadeAposentadoriaDesejada = 0;
        String[] opcoesValidas = {"Geral", "Professor", "Rural"};
        int idade;

        while (true) {
            System.out.print("Digite seu nome: ");
            nome = scanner.nextLine().trim();

            if (!nome.isEmpty() && nome.matches("[a-zA-ZÀ-ÿ^~\\s]+")) {
                break;
            }
            System.out.println("Nome inválido. Tente novamente.");
        }

        LocalDate dataAniversario = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        while (dataAniversario == null) {
            System.out.print("Digite sua data de nascimento (DD/MM/AAAA): ");
            dataNascimento = scanner.nextLine();
            try {
                dataAniversario = LocalDate.parse(dataNascimento, formatter);
                if (dataAniversario.isAfter(LocalDate.now())) {
                    System.out.println("Data de nascimento não pode ser maior que hoje.");
                    dataAniversario = null;
                }
            } catch (Exception e) {
                System.out.println("Data inválida. Use o formato DD/MM/AAAA.");
            }
        }

        System.out.println("Qual seu gênero? (Masculino ou Feminino)");
        while (true) {
            genero = scanner.nextLine();
            if (genero.equalsIgnoreCase("Masculino") || genero.equalsIgnoreCase("Feminino")) {
                genero = genero.substring(0, 1).toUpperCase() + genero.substring(1).toLowerCase();
                break;
            }
            System.out.println("Opção inválida. Digite Masculino ou Feminino.");
        }

        while (true) {
            System.out.println("Escolha sua profissão (Geral, Professor, Rural): ");
            profissao = scanner.nextLine().trim();
            if (isOpcaoValida(profissao, opcoesValidas)) {
                break;
            }
            System.out.println("Opção inválida! Escolha entre Geral, Professor ou Rural.");
        }

        while (true) {
            System.out.print("Digite a idade desejada para aposentadoria: ");
            try {
                idadeAposentadoriaDesejada = scanner.nextInt();
                if ((genero.equals("Masculino") && idadeAposentadoriaDesejada >= 65) ||
                        (genero.equals("Feminino") && idadeAposentadoriaDesejada >= 62)) {
                    break;
                }
                System.out.println("Idade inválida. Mínimo: 65 (Masculino) ou 62 (Feminino).");
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida! Digite um número inteiro.");
                scanner.next();
            }
        }

        return new Usuario(nome, dataNascimento, genero, profissao, idadeAposentadoriaDesejada);
    }

    private static boolean isOpcaoValida(String input, String[] opcoes) {
        for (String opcao : opcoes) {
            if (input.equalsIgnoreCase(opcao)) {
                return true;
            }
        }
        return false;
    }
    }