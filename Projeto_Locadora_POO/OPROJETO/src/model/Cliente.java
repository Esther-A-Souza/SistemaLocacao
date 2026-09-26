package model;

import database.BancoDados;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


//Classe que representa um cliente da locadora

public class Cliente {
    private int id;
    private String nome;
    private String cpf;
    private String telefone;
    private String email;
    private String endereco;

    public Cliente() {}

    public Cliente(int id, String nome, String cpf, String telefone, String email, String endereco) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
        this.endereco = endereco;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public boolean validarCampos() {
        if (nome == null || nome.trim().isEmpty()) {
            return false;
        }
        if (cpf == null || cpf.trim().isEmpty() || !cpf.matches("\\d{11}")) {
            return false;
        }
        return true;
    }

    public void incluir() throws SQLException {
        BancoDados bd = new BancoDados();
        bd.abrirConexao();
        String sql = "INSERT INTO cliente (nome, cpf, telefone, email, endereco) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = bd.getConexao().prepareStatement(sql);
        stmt.setString(1, nome);
        stmt.setString(2, cpf);
        stmt.setString(3, telefone);
        stmt.setString(4, email);
        stmt.setString(5, endereco);
        stmt.executeUpdate();
        stmt.close();
        bd.fecharConexao();
    }

    public void alterar() throws SQLException {
        BancoDados bd = new BancoDados();
        bd.abrirConexao();
        String sql = "UPDATE cliente SET nome=?, cpf=?, telefone=?, email=?, endereco=? WHERE id=?";
        PreparedStatement stmt = bd.getConexao().prepareStatement(sql);
        stmt.setString(1, nome);
        stmt.setString(2, cpf);
        stmt.setString(3, telefone);
        stmt.setString(4, email);
        stmt.setString(5, endereco);
        stmt.setInt(6, id);
        stmt.executeUpdate();
        stmt.close();
        bd.fecharConexao();
    }

    public void excluir() throws SQLException {
        BancoDados bd = new BancoDados();
        bd.abrirConexao();
        String sql = "DELETE FROM cliente WHERE id=?";
        PreparedStatement stmt = bd.getConexao().prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.executeUpdate();
        stmt.close();
        bd.fecharConexao();
    }

    public static List<Cliente> pesquisar(String texto) throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        BancoDados bd = new BancoDados();
        bd.abrirConexao();
        String sql = "SELECT * FROM cliente WHERE nome LIKE ? OR cpf LIKE ?";
        PreparedStatement stmt = bd.getConexao().prepareStatement(sql);
        stmt.setString(1, "%" + texto + "%");
        stmt.setString(2, "%" + texto + "%");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Cliente c = new Cliente();
            c.setId(rs.getInt("id"));
            c.setNome(rs.getString("nome"));
            c.setCpf(rs.getString("cpf"));
            c.setTelefone(rs.getString("telefone"));
            c.setEmail(rs.getString("email"));
            c.setEndereco(rs.getString("endereco"));
            clientes.add(c);
        }
        rs.close();
        stmt.close();
        bd.fecharConexao();
        return clientes;
    }

    public static List<Cliente> listarTodos() throws SQLException {
        return pesquisar("");
    }

    @Override
    public String toString() {
        return id + " - " + nome;
    }
}
