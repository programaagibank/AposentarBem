package com.AgiBank.controller.usuario;

import com.AgiBank.controller.contribuicao.ContribuicaoController;
import com.AgiBank.dao.contribuicao.ContribuicaoDAOImpl;
import com.AgiBank.dao.usuario.UsuarioDAOImpl;
import com.AgiBank.model.Usuario;
import com.AgiBank.view.ContribuicaoView;
import com.AgiBank.view.UsuarioView;

public class UsuarioController {
    private final UsuarioDAOImpl usuarioDAO;
    private final UsuarioView usuarioView;
    private final ContribuicaoDAOImpl contribuicaoDAO;
    private final ContribuicaoController contribuicaoController;
    private int idUsuarioAtual;


    public UsuarioController(UsuarioDAOImpl usuarioDAO, UsuarioView usuarioView, ContribuicaoDAOImpl contribuicaoDAO, ContribuicaoController contribuicaoController) {
        this.usuarioDAO = usuarioDAO;
        this.usuarioView = usuarioView;
        this.contribuicaoDAO = contribuicaoDAO;
        this.contribuicaoController = contribuicaoController;
    }

    public void criarUsuario() {
        Usuario usuario = usuarioView.coletarDadosUsuario(); // Cria o usuário através da view
        boolean sucesso = usuarioDAO.criarUsuario(usuario);

        if (sucesso) {
            idUsuarioAtual = usuario.getId();
            usuarioView.exibirMensagem("Usuário cadastrado com sucesso!\n");
            registrarContribuicao(usuario); // Registra as contribuições
        } else {
            usuarioView.exibirMensagem("Erro ao cadastrar usuário.");
        }
    }

    public int getIdUsuarioAtual() {
        return idUsuarioAtual;
    }

    public void consultarHistorico() {
        ContribuicaoView contribuicaoView = new ContribuicaoView(contribuicaoDAO, usuarioView.getUsuario(), contribuicaoController);
        contribuicaoView.consultarHistorico(idUsuarioAtual);
    }

    public void registrarContribuicao(Usuario usuario) {
        ContribuicaoView contribuicaoView = new ContribuicaoView(contribuicaoDAO, usuario, contribuicaoController);
        contribuicaoView.registrarContribuicao(idUsuarioAtual);
    }
}
