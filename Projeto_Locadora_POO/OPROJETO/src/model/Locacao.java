package model;

import database.BancoDados;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


 //Classe que representa uma locação de filme

public class Locacao {
    private int id;
    private int clienteId;
    private int filmeId;
    private LocalDate dataLocacao;
    private LocalDate dataDevolucaoPrevista;
    private LocalDate dataDevolucaoReal;
    private boolean devolvido;

    public Locacao() {}

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getClienteId() { return clienteId; }
    public void setClienteId(int clienteId) { this.clienteId = clienteId; }
    public int getFilmeId() { return filmeId; }
    public void setFilmeId(int filmeId) { this.filmeId = filmeId; }
    public LocalDate getDataLocacao() { return dataLocacao; }
    public void setDataLocacao(LocalDate dataLocacao) { this.dataLocacao = dataLocacao; }
    public LocalDate getDataDevolucaoPrevista() { return dataDevolucaoPrevista; }
    public void setDataDevolucaoPrevista(LocalDate dataDevolucaoPrevista) { this.dataDevolucaoPrevista = dataDevolucaoPrevista; }
    public LocalDate getDataDevolucaoReal() { return dataDevolucaoReal; }
    public void setDataDevolucaoReal(LocalDate dataDevolucaoReal) { this.dataDevolucaoReal = dataDevolucaoReal; }
    public boolean isDevolvido() { return devolvido; }
    public void setDevolvido(boolean devolvido) { this.devolvido = devolvido; }

    public boolean validarCampos() {
        return clienteId > 0 && filmeId > 0 && dataLocacao != null && dataDevolucaoPrevista != null;
    }

    public void incluir() throws SQLException {
        BancoDados bd = new BancoDados();
        bd.abrirConexao();
        String sql = "INSERT INTO locacao (cliente_id, filme_id, data_locacao, data_devolucao_prevista, devolvido) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = bd.getConexao().prepareStatement(sql);
        stmt.setInt(1, clienteId);
        stmt.setInt(2, filmeId);
        stmt.setDate(3, Date.valueOf(dataLocacao));
        stmt.setDate(4, Date.valueOf(dataDevolucaoPrevista));
        stmt.setBoolean(5, devolvido);
        stmt.executeUpdate();
        stmt.close();
        bd.fecharConexao();
    }

    public void registrarDevolucao() throws SQLException {
        BancoDados bd = new BancoDados();
        bd.abrirConexao();
        String sql = "UPDATE locacao SET devolvido=?, data_devolucao_real=? WHERE id=?";
        PreparedStatement stmt = bd.getConexao().prepareStatement(sql);
        stmt.setBoolean(1, true);
        stmt.setDate(2, Date.valueOf(LocalDate.now()));
        stmt.setInt(3, id);
        stmt.executeUpdate();
        stmt.close();
        bd.fecharConexao();
    }

    public static List<Locacao> listarTodas() throws SQLException {
        List<Locacao> locacoes = new ArrayList<>();
        BancoDados bd = new BancoDados();
        bd.abrirConexao();
        String sql = "SELECT * FROM locacao";
        Statement stmt = bd.getConexao().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            Locacao l = new Locacao();
            l.setId(rs.getInt("id"));
            l.setClienteId(rs.getInt("cliente_id"));
            l.setFilmeId(rs.getInt("filme_id"));
            l.setDataLocacao(rs.getDate("data_locacao").toLocalDate());
            l.setDataDevolucaoPrevista(rs.getDate("data_devolucao_prevista").toLocalDate());
            if (rs.getDate("data_devolucao_real") != null) {
                l.setDataDevolucaoReal(rs.getDate("data_devolucao_real").toLocalDate());
            }
            l.setDevolvido(rs.getBoolean("devolvido"));
            locacoes.add(l);
        }
        rs.close();
        stmt.close();
        bd.fecharConexao();
        return locacoes;
    }
}
