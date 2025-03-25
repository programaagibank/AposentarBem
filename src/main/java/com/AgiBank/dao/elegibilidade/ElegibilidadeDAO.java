package com.AgiBank.dao.elegibilidade;

import java.sql.SQLException;

public interface ElegibilidadeDAO {
    int obterUltimoIdUsuario() throws SQLException;
    int calcularMesesTrabalhadosAntesDe2019(int idUsuario);
    boolean wasEligibleBefore2019(int idUsuario);
    String[] obterDadosUsuario(int idUsuario);
    int aposentarIdadeMinimaETempo(int idUsuario);
    double calcularValorAposentadoria(int idUsuario);
}
