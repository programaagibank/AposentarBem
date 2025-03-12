package com.AgiBank.controller.usuario;

import com.AgiBank.dao.usuario.UsuarioDAO;
import com.AgiBank.model.Usuario;
import com.AgiBank.view.UsuarioView;

import java.util.Scanner;

public class UsuarioController {
    private UsuarioDAO usuarioDAO;
    private UsuarioView usuarioView;

    public UsuarioController(UsuarioDAO usuarioDAO, UsuarioView usuarioView) {
        this.usuarioDAO = usuarioDAO;
        this.usuarioView = usuarioView;
    }

    public void criarUsuario() {
        Scanner scanner = new Scanner(System.in);
        Usuario usuario = UsuarioView.colectUserDataFromConsole(scanner);

        usuarioDAO.criarUsuario(usuario);
        System.out.println("Usuário cadastrado com sucesso!");

        scanner.close();
    }
}
