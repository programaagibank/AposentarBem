package com.AgiBank.controller.usuario;

import com.AgiBank.dao.usuario.UsuarioDAO;
import com.AgiBank.model.Usuario;
import com.AgiBank.view.UsuarioView;

public class UsuarioController {
    private UsuarioDAO usuarioDAO;
    private UsuarioView usuarioView;

    public UsuarioController(UsuarioDAO usuarioDAO, UsuarioView usuarioView) {
        this.usuarioDAO = usuarioDAO;
        this.usuarioView = usuarioView;
    }

    public void criarUsuario() {
        Usuario usuario = usuarioView.coletarDadosUsuario();
        usuarioDAO.criarUsuario(usuario);
        usuarioView.exibirMensagem("Usuário cadastrado com sucesso!");

        //teste
        System.out.println("Idade do usuário: " + usuario.getIdade() + " anos.");
    }
}
