package com.AgiBank.controller.Elegibilidade;

import com.AgiBank.dao.elegibilidade.*;
import java.sql.SQLException;

public class ElegibilidadeController {
    private final ElegibilidadeDAOImpl elegibilidadeDAO;

    public ElegibilidadeController() {
        this.elegibilidadeDAO = new ElegibilidadeDAOImpl();
    }

    public void verificarUltimoUsuario() {
        try {
            int ultimoId = elegibilidadeDAO.obterUltimoIdUsuario();
            if (ultimoId == -1) {
                System.out.println("Nenhum usuário encontrado no banco.");
                return;
            }

            boolean elegivel = elegibilidadeDAO.wasEligibleBefore2019(ultimoId);

            if (elegivel) {
                System.out.println("O último usuário cadastrado já podia se aposentar antes de 2019.");
            } else {
                System.out.println("O último usuário cadastrado NÃO podia se aposentar antes de 2019.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao verificar a elegibilidade: " + e.getMessage());
        }
    }
}
