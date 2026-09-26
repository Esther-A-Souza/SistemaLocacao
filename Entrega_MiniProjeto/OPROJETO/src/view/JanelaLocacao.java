package view;

import model.Cliente;
import model.Filme;
import model.Locacao;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Janela para realizar locações de filmes
 */
public class JanelaLocacao extends JFrame {
    private JComboBox<Cliente> cmbCliente;
    private JComboBox<Filme> cmbFilme;
    private JTextField txtDataLocacao;
    private JTextField txtDataDevolucao;
    private JButton btnRealizarLocacao;
    private JButton btnCancelar;

    public JanelaLocacao() {
        setTitle("Realizar Locação");
        setSize(500, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        inicializarComponentes();
        carregarDados();
    }

    private void inicializarComponentes() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel lblTitulo = new JLabel("Realizar Locação de Filme", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        painel.add(lblTitulo, gbc);

        // Cliente
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        painel.add(new JLabel("Cliente:"), gbc);
        gbc.gridx = 1;
        cmbCliente = new JComboBox<>();
        painel.add(cmbCliente, gbc);

        // Filme
        gbc.gridx = 0; gbc.gridy = 2;
        painel.add(new JLabel("Filme:"), gbc);
        gbc.gridx = 1;
        cmbFilme = new JComboBox<>();
        painel.add(cmbFilme, gbc);

        // Data Locação
        gbc.gridx = 0; gbc.gridy = 3;
        painel.add(new JLabel("Data Locação:"), gbc);
        gbc.gridx = 1;
        txtDataLocacao = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        txtDataLocacao.setEditable(false);
        painel.add(txtDataLocacao, gbc);

        // Data Devolução Prevista
        gbc.gridx = 0; gbc.gridy = 4;
        painel.add(new JLabel("Devolução Prevista:"), gbc);
        gbc.gridx = 1;
        txtDataDevolucao = new JTextField(LocalDate.now().plusDays(7).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        txtDataDevolucao.setEditable(false);
        painel.add(txtDataDevolucao, gbc);

        // Botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnRealizarLocacao = new JButton("Realizar Locação");
        btnCancelar = new JButton("Cancelar");

        btnRealizarLocacao.addActionListener(e -> realizarLocacao());
        btnCancelar.addActionListener(e -> dispose());

        painelBotoes.add(btnRealizarLocacao);
        painelBotoes.add(btnCancelar);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        painel.add(painelBotoes, gbc);

        add(painel);
    }

    private void carregarDados() {
        try {
            // Carregar clientes
            List<Cliente> clientes = Cliente.listarTodos();
            for (Cliente c : clientes) {
                cmbCliente.addItem(c);
            }

            // Carregar filmes disponíveis
            List<Filme> filmes = Filme.listarTodos();
            for (Filme f : filmes) {
                if (f.isDisponivel()) {
                    cmbFilme.addItem(f);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void realizarLocacao() {
        if (cmbCliente.getSelectedItem() == null || cmbFilme.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente e um filme!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Cliente clienteSelecionado = (Cliente) cmbCliente.getSelectedItem();
            Filme filmeSelecionado = (Filme) cmbFilme.getSelectedItem();

            Locacao locacao = new Locacao();
            locacao.setClienteId(clienteSelecionado.getId());
            locacao.setFilmeId(filmeSelecionado.getId());
            locacao.setDataLocacao(LocalDate.now());
            locacao.setDataDevolucaoPrevista(LocalDate.now().plusDays(7));
            locacao.setDevolvido(false);

            // Verificar se o filme está disponível
            if (!filmeSelecionado.isDisponivel()) {
                JOptionPane.showMessageDialog(this, "Este filme não está disponível no momento!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Realizar a locação
            locacao.incluir();

            // Atualizar disponibilidade do filme
            filmeSelecionado.setDisponivel(false);
            filmeSelecionado.alterar();

            JOptionPane.showMessageDialog(this, "Locação realizada com sucesso!");
            dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao realizar locação: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
