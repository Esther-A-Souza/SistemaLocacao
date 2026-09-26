package view;

import model.Usuario;
import util.Mensagens;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

//Janela de autenticação do sistema

public class JanelaLogin extends JFrame {
    private JTextField txtLogin;
    private JPasswordField txtSenha;
    private JButton btnEntrar;
    private JButton btnSair;

    public JanelaLogin() {
        setTitle("Sistema de Locação de Filmes - Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        JPanel painelPrincipal = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel lblTitulo = new JLabel("LOGIN DO SISTEMA", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        painelPrincipal.add(lblTitulo, gbc);

        // Login
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        painelPrincipal.add(new JLabel("Usuário:"), gbc);

        gbc.gridx = 1;
        txtLogin = new JTextField(20);
        painelPrincipal.add(txtLogin, gbc);

        // Senha
        gbc.gridx = 0;
        gbc.gridy = 2;
        painelPrincipal.add(new JLabel("Senha:"), gbc);

        gbc.gridx = 1;
        txtSenha = new JPasswordField(20);
        painelPrincipal.add(txtSenha, gbc);

        // Botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnEntrar = new JButton("Entrar");
        btnSair = new JButton("Sair");

        btnEntrar.addActionListener(e -> realizarLogin());
        btnSair.addActionListener(e -> System.exit(0));

        painelBotoes.add(btnEntrar);
        painelBotoes.add(btnSair);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        painelPrincipal.add(painelBotoes, gbc);

        add(painelPrincipal);
    }

    private void realizarLogin() {
        String login = txtLogin.getText();
        String senha = new String(txtSenha.getPassword());

        if (login.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (Usuario.autenticar(login, senha)) {
                JOptionPane.showMessageDialog(this, "Login realizado com sucesso!");
                this.dispose();
                new JanelaMenu().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Usuário ou senha inválidos!", "Erro", JOptionPane.ERROR_MESSAGE);
                txtSenha.setText("");
                txtLogin.requestFocus();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar com o banco de dados: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JanelaLogin().setVisible(true));
    }
}
