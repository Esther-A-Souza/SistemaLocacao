package model;

import database.BancoDados;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//Classe que representa um filme da locadora

public class Filme {
    private int id;
    private String titulo;
    private String genero;
    private int ano;
    private String diretor;
    private String idioma;
    private boolean disponivel;

    public Filme() {}

    public Filme(int id, String titulo, String genero, int ano, String diretor, String idioma, boolean disponivel) {
        this.id = id;
        this.titulo = titulo;
        this.genero = genero;
        this.ano = ano;
        this.diretor = diretor;
        this.idioma = idioma;
        this.disponivel = disponivel;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
    public int getAno() { return ano; }
    public void setAno(int ano) { this.ano = ano; }
    public String getDiretor() { return diretor; }
    public void setDiretor(String diretor) { this.diretor = diretor; }
    public String getIdioma() { return idioma; }
    public void setIdioma(String idioma) { this.idioma = idioma; }
    public boolean isDisponivel() { return disponivel; }
    public void setDisponivel(boolean disponivel) { this.disponivel = disponivel; }

    public boolean validarCampos() {
        if (titulo == null || titulo.trim().isEmpty()) {
            return false;
        }
        if (ano < 1900 || ano > 2030) {
            return false;
        }
        return true;
    }

    public void incluir() throws SQLException {
        BancoDados bd = new BancoDados();
        bd.abrirConexao();
        String sql = "INSERT INTO filme (titulo, genero, ano, diretor, idioma, disponivel) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = bd.getConexao().prepareStatement(sql);
        stmt.setString(1, titulo);
        stmt.setString(2, genero);
        stmt.setInt(3, ano);
        stmt.setString(4, diretor);
        stmt.setString(5, idioma);
        stmt.setBoolean(6, disponivel);
        stmt.executeUpdate();
        stmt.close();
        bd.fecharConexao();
    }

    public void alterar() throws SQLException {
        BancoDados bd = new BancoDados();
        bd.abrirConexao();
        String sql = "UPDATE filme SET titulo=?, genero=?, ano=?, diretor=?, idioma=?, disponivel=? WHERE id=?";
        PreparedStatement stmt = bd.getConexao().prepareStatement(sql);
        stmt.setString(1, titulo);
        stmt.setString(2, genero);
        stmt.setInt(3, ano);
        stmt.setString(4, diretor);
        stmt.setString(5, idioma);
        stmt.setBoolean(6, disponivel);
        stmt.setInt(7, id);
        stmt.executeUpdate();
        stmt.close();
        bd.fecharConexao();
    }

    public void excluir() throws SQLException {
        BancoDados bd = new BancoDados();
        bd.abrirConexao();
        String sql = "DELETE FROM filme WHERE id=?";
        PreparedStatement stmt = bd.getConexao().prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.executeUpdate();
        stmt.close();
        bd.fecharConexao();
    }

    public static List<Filme> pesquisarPorCampo(String campo, String texto) throws SQLException {
        List<Filme> filmes = new ArrayList<>();
        BancoDados bd = new BancoDados();
        bd.abrirConexao();
        String sql = "";

        switch (campo) {
            case "ID":
                sql = "SELECT * FROM filme WHERE id = ?";
                break;
            case "Título":
                sql = "SELECT * FROM filme WHERE titulo LIKE ?";
                break;
            case "Gênero":
                sql = "SELECT * FROM filme WHERE genero LIKE ?";
                break;
            default:
                sql = "SELECT * FROM filme WHERE titulo LIKE ? OR genero LIKE ?";
        }

        PreparedStatement stmt = bd.getConexao().prepareStatement(sql);
        if (campo.equals("ID")) {
            try {
                stmt.setInt(1, Integer.parseInt(texto));
            } catch (NumberFormatException e) {
                stmt.setInt(1, -1);
            }
        } else if (campo.equals("Título") || campo.equals("Gênero")) {
            stmt.setString(1, "%" + texto + "%");
        } else {
            stmt.setString(1, "%" + texto + "%");
            stmt.setString(2, "%" + texto + "%");
        }

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Filme f = new Filme();
            f.setId(rs.getInt("id"));
            f.setTitulo(rs.getString("titulo"));
            f.setGenero(rs.getString("genero"));
            f.setAno(rs.getInt("ano"));
            f.setDiretor(rs.getString("diretor"));
            f.setIdioma(rs.getString("idioma"));
            f.setDisponivel(rs.getBoolean("disponivel"));
            filmes.add(f);
        }
        rs.close();
        stmt.close();
        bd.fecharConexao();
        return filmes;
    }

    public static List<Filme> listarTodos() throws SQLException {
        return pesquisarPorCampo("Todos", "");
    }

    @Override
    public String toString() {
        return id + " - " + titulo;
    }
}
