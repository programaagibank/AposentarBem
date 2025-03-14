package com.AgiBank;

import com.AgiBank.controller.usuario.UsuarioController;
import com.AgiBank.dao.usuario.UsuarioDAOImpl;
import com.AgiBank.view.UsuarioView;
import com.AgiBank.model.Usuario;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        UsuarioDAOImpl usuarioDAO = new UsuarioDAOImpl();
        UsuarioView usuarioView = new UsuarioView();
        UsuarioController usuarioController = new UsuarioController(usuarioDAO, usuarioView);
        usuarioController.criarUsuario();
    }}