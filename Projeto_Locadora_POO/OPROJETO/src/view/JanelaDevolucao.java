package view;

import model.Filme;
import model.Locacao;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

// Janela para realizar devoluções de filmes
 
public class JanelaDevolucao extends JFrame {
    private JTable tabelaLocacoes;
    private DefaultTableModel modeloTabela;
    private JButton btnDevolucao;
    private JButton btnAtualizar;

    public JanelaDevolucao() {
        setTitle("Realizar Devolução");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        inicializarComponentes();
        carregarLocacoes();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));

        // Título
        JLabel lblTitulo = new JLabel("Locações Pendentes de Devolução", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(lblTitulo, BorderLayout.NORTH);

        // Tabela
        String[] colunas = {"ID Locação", "ID Cliente", "ID Filme", "Data Locação", "Devolução Prevista", "Status"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaLocacoes = new JTable(modeloTabela);
        JScrollPane scrollPane = new JScrollPane(tabelaLocacoes);
        add(scrollPane, BorderLayout.CENTER);

        // Painel de botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnDevolucao = new JButton("Registrar Devolução");
        btnAtualizar = new JButton("Atualizar Lista");

        btnDevolucao.addActionListener(e -> registrarDevolucao());
        btnAtualizar.addActionListener(e -> carregarLocacoes());

        painelBotoes.add(btnDevolucao);
        painelBotoes.add(btnAtualizar);

        add(painelBotoes, BorderLayout.SOUTH);
    }

    private void carregarLocacoes() {
        try {
            List<Locacao> locacoes = Locacao.listarTodas();
            modeloTabela.setRowCount(0);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            for (Locacao loc : locacoes) {
                if (!loc.isDevolvido()) {
                    modeloTabela.addRow(new Object[]{
                            loc.getId(),
                            loc.getClienteId(),
                            loc.getFilmeId(),
                            loc.getDataLocacao().format(formatter),
                            loc.getDataDevolucaoPrevista().format(formatter),
                            "Pendente"
                    });
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar locações: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarDevolucao() {
        int linhaSelecionada = tabelaLocacoes.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma locação para devolver!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idLocacao = (int) modeloTabela.getValueAt(linhaSelecionada, 0);
        int idFilme = (int) modeloTabela.getValueAt(linhaSelecionada, 2);

        int opcao = JOptionPane.showConfirmDialog(this, "Confirma a devolução do filme?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (opcao == JOptionPane.YES_OPTION) {
            try {
                // Atualizar locação
                Locacao locacao = new Locacao();
                locacao.setId(idLocacao);
                locacao.registrarDevolucao();

                // Atualizar disponibilidade do filme
                List<Filme> filmes = Filme.pesquisarPorCampo("ID", String.valueOf(idFilme));
                if (!filmes.isEmpty()) {
                    Filme filme = filmes.get(0);
                    filme.setDisponivel(true);
                    filme.alterar();
                }

                JOptionPane.showMessageDialog(this, "Devolução registrada com sucesso!");
                carregarLocacoes();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao registrar devolução: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
