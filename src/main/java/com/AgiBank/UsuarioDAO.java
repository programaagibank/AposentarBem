package com.AgiBank;

import com.AgiBank.DB.DatabaseConnection;
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UsuarioDAO {
    private final String url;
    private final String username;
    private final String password;

    public UsuarioDAO() {
        Dotenv dotenv = Dotenv.load();
        String port = dotenv.get("DB_PORT");
        String baseUrl = dotenv.get("DB_HOST");
        this.username = dotenv.get("DB_USER");
        this.password = dotenv.get("DB_PASS");

        this.url = "jdbc:mysql://" + baseUrl + ":" + port + "/aposentarBem";
    }

    public void salvarUsuario(Usuario usuario) {
        String sql = "INSERT INTO Usuario (nome, dataNascimento, genero, profissao, idadeAposentadoriaDesejada) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNome());
            stmt.setDate(2, java.sql.Date.valueOf(usuario.getDataNascimento())); // Certifique-se de que está no formato correto
            stmt.setString(3, usuario.getGenero());
            stmt.setString(4, usuario.getProfissao());
            stmt.setInt(5, usuario.getIdadeAposentadoriaDesejada());

            stmt.executeUpdate();
            System.out.println("Usuário salvo com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
