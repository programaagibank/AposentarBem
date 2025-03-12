package com.AgiBank.model;

import java.time.LocalDate;

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
}
