package com.AgiBank.dao.usuario;

import com.AgiBank.model.Usuario;
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UsuarioDAOImpl implements UsuarioDAO {

    private final String url;
    private final String username;
    private final String password;

    public UsuarioDAOImpl() {
        Dotenv dotenv = Dotenv.load();
        String port = dotenv.get("DATABASE_PORT");
        String baseUrl = dotenv.get("DATABASE_URL");
        this.username = dotenv.get("DATABASE_USERNAME");
        this.password = dotenv.get("DATABASE_PASSWORD");

        this.url = "jdbc:mysql://" + baseUrl + ":" + port + "/aposentarBem";
    }

    @Override
    public void criarUsuario(Usuario usuario) {
        String dataNascimento = usuario.getDataNascimento();
        LocalDate localDate = LocalDate.parse(dataNascimento, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String sql = "INSERT INTO Usuario (nome, dataNascimento, genero, profissao, idadeAposentadoriaDesejada) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNome());
            stmt.setDate(2, java.sql.Date.valueOf(localDate));
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
