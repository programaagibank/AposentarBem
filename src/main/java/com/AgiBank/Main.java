package com.AgiBank;

import com.AgiBank.controller.contribuicao.ContribuicaoController;
import com.AgiBank.dao.contribuicao.ContribuicaoDAOImpl;
import com.AgiBank.dao.usuario.UsuarioDAOImpl;
import com.AgiBank.view.ContribuicaoView;
import com.AgiBank.controller.usuario.UsuarioController;
import com.AgiBank.view.UsuarioView;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        System.setErr(new PrintStream(System.err, true, StandardCharsets.UTF_8));

        // Inicialização dos DAOs
        UsuarioDAOImpl usuarioDAO = new UsuarioDAOImpl();
        ContribuicaoDAOImpl contribuicaoDAO = new ContribuicaoDAOImpl();

        // Criação da view do usuário
        UsuarioView usuarioView = new UsuarioView();
        ContribuicaoView contribuicaoView = new ContribuicaoView(contribuicaoDAO, usuarioView.getUsuario(), null);  // Aqui passamos ContribuicaoView corretamente
        ContribuicaoController contribuicaoController = new ContribuicaoController(contribuicaoDAO, contribuicaoView);

        UsuarioController usuarioController = new UsuarioController(usuarioDAO, usuarioView, contribuicaoDAO, contribuicaoController);
        usuarioController.criarUsuario();

        // Verificar se o usuário foi criado e consultar o histórico de contribuições
        if (usuarioController.getIdUsuarioAtual() != 0) {
            usuarioController.consultarHistorico();
        } else {
            System.out.println("Erro: Nenhum usuário foi criado. Impossível consultar histórico.");
        }

    }
}

