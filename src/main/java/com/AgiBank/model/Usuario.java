package com.AgiBank.model;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class Usuario {
    private String nome;
    private String dataNascimento;
    private String genero;
    private String profissao;
    private int idadeAposentadoriaDesejada;
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

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public static boolean validarNome(String nome) {
        return !nome.isEmpty() && nome.matches("[a-zA-ZÀ-ÿ\\s]+");
    }

    public static boolean validarDataNascimento(String dataNascimento) {
        LocalDate hoje = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            LocalDate dataAniversario = LocalDate.parse(dataNascimento, formatter);
            return !dataAniversario.isAfter(hoje);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean validarGenero(String genero) {
        return genero.equalsIgnoreCase("Masculino") || genero.equalsIgnoreCase("Feminino");
    }

    public static boolean validarProfissao(String profissao) {
        String[] opcoesValidas = {"Geral", "Professor", "Rural"};
        for (String opcao : opcoesValidas) {
            if (profissao.equalsIgnoreCase(opcao)) {
                return true;
            }
        }
        return false;
    }

    public static boolean validarIdadeAposentadoria(int idadeAposentadoria) {
        int idadeMinima = 40;
        return idadeAposentadoria >= idadeMinima && idadeAposentadoria < 90;
    }
}


