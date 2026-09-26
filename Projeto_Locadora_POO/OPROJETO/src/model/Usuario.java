package model;

import database.BancoDados;
import java.sql.*;


 //Classe que representa um usuário do sistema

public class Usuario {
    private int id;
    private String login;
    private String senha;
    private String nome;

    public Usuario() {}

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public static boolean autenticar(String login, String senha) throws SQLException {
        BancoDados bd = new BancoDados();
        bd.abrirConexao();
        String sql = "SELECT * FROM usuario WHERE login=? AND senha=?";
        PreparedStatement stmt = bd.getConexao().prepareStatement(sql);
        stmt.setString(1, login);
        stmt.setString(2, senha);
        ResultSet rs = stmt.executeQuery();
        boolean autenticado = rs.next();
        rs.close();
        stmt.close();
        bd.fecharConexao();
        return autenticado;
    }
}
