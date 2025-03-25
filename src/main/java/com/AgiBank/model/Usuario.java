package com.AgiBank.model;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Usuario {
    private int id;
    private String nome;
    private LocalDate dataNascimento;
    private int idade;
    private String genero;
    private String profissao;
    private int idadeAposentadoriaDesejada;

    public Usuario(String nome, String dataNascimento, String genero, String profissao, int idadeAposentadoriaDesejada) {
        this.nome = nome;
        setDataNascimento(dataNascimento);
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

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimentoStr) {
        this.dataNascimento = validarData(dataNascimentoStr);
        this.idade = calcularIdade(this.dataNascimento);
    }

    public static LocalDate validarData(String dataNascimentoStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            LocalDate dataConvertida = LocalDate.parse(dataNascimentoStr, formatter);
            LocalDate dataAtual = LocalDate.now();

            if (dataConvertida.isAfter(dataAtual)) {
                throw new IllegalArgumentException("A data de nascimento não pode ser no futuro.");
            }
            int idade = calcularIdade(dataConvertida);
            if (idade < 15) {
                throw new IllegalArgumentException("A idade mínima permitida para simulação é 15 anos.");
            }
            return dataConvertida;
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de data inválido! Use o formato DD/MM/AAAA.");
        }
    }

    public static int calcularIdade(LocalDate dataNascimento) {
        if (dataNascimento == null) {
            throw new IllegalStateException("Data de nascimento não foi definida.");
        }
        LocalDate dataAtual = LocalDate.now();
        return Period.between(dataNascimento, dataAtual).getYears();
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
