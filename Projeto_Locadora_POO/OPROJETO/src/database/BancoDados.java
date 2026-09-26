package database;

import java.sql.*;

//Classe responsável pelo gerenciamento de conexões com o banco de dados MySQL

    public class BancoDados {
        private static final String URL = "jdbc:mysql://localhost:3306/locadora_filmes";
        private static final String USUARIO = "root";
        private static final String SENHA = "root";
        private Connection conexao;

        public void abrirConexao() throws SQLException {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver MySQL não encontrado: " + e.getMessage());
            }
        }

        public void fecharConexao() throws SQLException {
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
            }
        }

        public Connection getConexao() {
            return conexao;
        }
    }


