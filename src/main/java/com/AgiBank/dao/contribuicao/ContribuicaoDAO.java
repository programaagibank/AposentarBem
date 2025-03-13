package com.AgiBank.dao.contribuicao;

import java.sql.SQLException;
import java.time.LocalDate;

public interface ContribuicaoDAO {
    void registrarContribuicao(int idContribuicao, int idUsuario, double valorSalario, LocalDate periodoInicio,
                               LocalDate periodoFim) throws SQLException;
    void consultarHistorico(int idUsuario) throws SQLException;
}