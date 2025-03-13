package com.AgiBank;

import com.AgiBank.view.ContribuicaoView;
import com.AgiBank.controller.usuario.UsuarioController;
import com.AgiBank.dao.usuario.UsuarioDAOImpl;
import com.AgiBank.utils.DatabaseConnection;
import com.AgiBank.view.UsuarioView;
import com.AgiBank.model.Usuario;

public class Main {
    public static void main(String[] args) {
      
      //Criação usuario
        UsuarioDAOImpl usuarioDAO = new UsuarioDAOImpl();
        UsuarioView usuarioView = new UsuarioView();
        UsuarioController usuarioController = new UsuarioController(usuarioDAO, usuarioView);
        usuarioController.criarUsuario();
      
      //Recolhimento de contribuição
       ContribuicaoView contribuicaoView = new ContribuicaoView();
        contribuicaoView.registrarContribuicao();
        contribuicaoView.consultarHistorico();
    }
}
