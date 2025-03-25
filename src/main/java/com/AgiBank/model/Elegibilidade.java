package com.AgiBank.model;

public class Elegibilidade {
    private int idUsuario;
    private int idade;
    private String genero;
    private String profissao;
    private int mesesContribuidos;
    private boolean isElegivel = false;

    public Elegibilidade(int idUsuario, int idade, int mesesContribuidos, String genero, String profissao, boolean isElegivel) {
        this.idUsuario = idUsuario;
        this.idade = idade;
        this.mesesContribuidos = mesesContribuidos;
        this.genero = genero;
        this.profissao = profissao;
        this.isElegivel = isElegivel;
    }

    public int getidUsuario() { return idUsuario; }
    public int getIdade() { return idade; }
    public int getMesesTrabalhadosAntes2019() { return mesesContribuidos; }
    public String getGenero() { return genero; }
    public String getProfissao() { return profissao; }
}
