package com.AgiBank.dao.elegibilidade;

import java.sql.SQLException;

public interface ElegibilidadeDAO {
    int obterUltimoIdUsuario() throws SQLException;
    int calcularMesesTrabalhadosAntesDe2019(int userId);
    boolean wasEligibleBefore2019(int userId);
    int calcularIdadeEm2019(String dataNascimento, int anoReferencia);
    String[] obterDadosUsuario(int userId);
}
