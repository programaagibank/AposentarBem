package com.AgiBank.model;

public class Elegibilidade {
    private int userId;
    private int idadeEm2019;
    private int mesesTrabalhadosAntes2019;
    private String genero;
    private String profissao;

    public Elegibilidade(int userId, int idadeEm2019, int mesesTrabalhadosAntes2019, String genero, String profissao) {
        this.userId = userId;
        this.idadeEm2019 = idadeEm2019;
        this.mesesTrabalhadosAntes2019 = mesesTrabalhadosAntes2019;
        this.genero = genero;
        this.profissao = profissao;
    }

    public int getUserId() { return userId; }
    public int getIdadeEm2019() { return idadeEm2019; }
    public int getMesesTrabalhadosAntes2019() { return mesesTrabalhadosAntes2019; }
    public String getGenero() { return genero; }
    public String getProfissao() { return profissao; }
}
