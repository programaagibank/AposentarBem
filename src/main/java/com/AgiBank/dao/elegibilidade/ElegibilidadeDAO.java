package com.AgiBank.dao.elegibilidade;

import java.sql.SQLException;

public interface ElegibilidadeDAO {
    int calcularMesesTrabalhadosAntesDe2019(int userId);
    int calcularIdade(String dataNascimento, int anoReferencia);
    boolean wasEligibleBefore2019(int userId);
    public void verificarUltimoUsuario() throws SQLException;
    int obterUltimoIdUsuario() throws SQLException;
}
