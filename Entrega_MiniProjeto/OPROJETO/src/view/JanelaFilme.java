package view;

import model.Filme;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

//Janela para gerenciamento de filmes com pesquisa complexa

public class JanelaFilme extends JFrame {
    private JTabbedPane abas;
    private JTextField txtId, txtTitulo, txtGenero, txtAno, txtDiretor, txtIdioma;
    private JCheckBox chkDisponivel;
    private JTextField txtPesquisa;
    private JComboBox<String> cmbCampoPesquisa;
    private JTable tabelaPesquisa;
    private DefaultTableModel modeloTabela;
    private JButton btnIncluir, btnAlterar, btnExcluir, btnLimpar;
    private JButton btnPesquisar;
    private Filme filmeSelecionado;

    public JanelaFilme() {
        setTitle("Gerenciamento de Filmes");
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        abas = new JTabbedPane();

        // Aba de Cadastro
        JPanel painelCadastro = criarPainelCadastro();
        abas.addTab("Cadastro", painelCadastro);

        // Aba de Pesquisa (com filtros)
        JPanel painelPesquisa = criarPainelPesquisa();
        abas.addTab("Pesquisa com Filtros", painelPesquisa);

        add(abas);
    }

    private JPanel criarPainelCadastro() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Painel de campos
        JPanel painelCampos = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ID
        gbc.gridx = 0; gbc.gridy = 0;
        painelCampos.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        txtId = new JTextField(10);
        txtId.setEditable(false);
        painelCampos.add(txtId, gbc);

        // Título
        gbc.gridx = 0; gbc.gridy = 1;
        painelCampos.add(new JLabel("Título:*"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        txtTitulo = new JTextField(30);
        painelCampos.add(txtTitulo, gbc);

        // Gênero
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        painelCampos.add(new JLabel("Gênero:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        txtGenero = new JTextField(20);
        painelCampos.add(txtGenero, gbc);

        // Ano
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1;
        painelCampos.add(new JLabel("Ano:*"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 1;
        txtAno = new JTextField(10);
        // Validação para aceitar apenas números
        txtAno.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!Character.isDigit(c)) {
                    evt.consume();
                }
            }
        });
        painelCampos.add(txtAno, gbc);

        // Diretor
        gbc.gridx = 0; gbc.gridy = 4;
        painelCampos.add(new JLabel("Diretor:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        txtDiretor = new JTextField(30);
        painelCampos.add(txtDiretor, gbc);

        // Idioma
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 1;
        painelCampos.add(new JLabel("Idioma:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        txtIdioma = new JTextField(20);
        painelCampos.add(txtIdioma, gbc);

        // Disponível
        gbc.gridx = 0; gbc.gridy = 6;
        painelCampos.add(new JLabel("Disponível:"), gbc);
        gbc.gridx = 1;
        chkDisponivel = new JCheckBox();
        chkDisponivel.setSelected(true);
        painelCampos.add(chkDisponivel, gbc);

        painel.add(painelCampos, BorderLayout.NORTH);

        // Painel de botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnIncluir = new JButton("Incluir");
        btnAlterar = new JButton("Alterar");
        btnExcluir = new JButton("Excluir");
        btnLimpar = new JButton("Limpar");

        btnIncluir.addActionListener(e -> incluirFilme());
        btnAlterar.addActionListener(e -> alterarFilme());
        btnExcluir.addActionListener(e -> excluirFilme());
        btnLimpar.addActionListener(e -> limparCampos());

        painelBotoes.add(btnIncluir);
        painelBotoes.add(btnAlterar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnLimpar);

        painel.add(painelBotoes, BorderLayout.SOUTH);

        return painel;
    }

    private JPanel criarPainelPesquisa() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Painel de pesquisa com filtros
        JPanel painelPesquisaTopo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelPesquisaTopo.add(new JLabel("Pesquisar por:"));

        cmbCampoPesquisa = new JComboBox<>(new String[]{"Todos", "ID", "Título", "Gênero"});
        cmbCampoPesquisa.addActionListener(e -> validarCampoPesquisa());
        painelPesquisaTopo.add(cmbCampoPesquisa);

        painelPesquisaTopo.add(new JLabel("Texto:"));
        txtPesquisa = new JTextField(25);
        painelPesquisaTopo.add(txtPesquisa);

        btnPesquisar = new JButton("Pesquisar");
        btnPesquisar.addActionListener(e -> pesquisarFilmes());
        painelPesquisaTopo.add(btnPesquisar);

        painel.add(painelPesquisaTopo, BorderLayout.NORTH);

        // Tabela de resultados
        String[] colunas = {"ID", "Título", "Gênero", "Ano", "Diretor", "Idioma", "Disponível"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaPesquisa = new JTable(modeloTabela);
        tabelaPesquisa.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabelaPesquisa.getSelectedRow() != -1) {
                carregarFilmeSelecionado();
            }
        });

        JScrollPane scrollPane = new JScrollPane(tabelaPesquisa);
        painel.add(scrollPane, BorderLayout.CENTER);

        return painel;
    }

    private void validarCampoPesquisa() {
        String campoSelecionado = (String) cmbCampoPesquisa.getSelectedItem();
        if (campoSelecionado.equals("ID")) {
            // Remove listener anterior se existir
            for (java.awt.event.KeyListener kl : txtPesquisa.getKeyListeners()) {
                txtPesquisa.removeKeyListener(kl);
            }
            // Adiciona validação para aceitar apenas números
            txtPesquisa.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent evt) {
                    char c = evt.getKeyChar();
                    if (!Character.isDigit(c) && c != '\b') {
                        evt.consume();
                        JOptionPane.showMessageDialog(JanelaFilme.this,
                                "Para pesquisa por ID, digite apenas números!",
                                "Validação", JOptionPane.WARNING_MESSAGE);
                    }
                }
            });
        } else {
            // Remove validação numérica
            for (java.awt.event.KeyListener kl : txtPesquisa.getKeyListeners()) {
                txtPesquisa.removeKeyListener(kl);
            }
        }
        txtPesquisa.setText("");
    }

    private void incluirFilme() {
        try {
            if (txtAno.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "O campo Ano é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Filme filme = new Filme();
            filme.setTitulo(txtTitulo.getText());
            filme.setGenero(txtGenero.getText());
            filme.setAno(Integer.parseInt(txtAno.getText()));
            filme.setDiretor(txtDiretor.getText());
            filme.setIdioma(txtIdioma.getText());
            filme.setDisponivel(chkDisponivel.isSelected());

            if (!filme.validarCampos()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios corretamente!\nAno deve estar entre 1900 e 2030.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            filme.incluir();
            JOptionPane.showMessageDialog(this, "Filme incluído com sucesso!");
            limparCampos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ano inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao incluir filme: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void alterarFilme() {
        if (filmeSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um filme na aba de pesquisa!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (txtAno.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "O campo Ano é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            filmeSelecionado.setTitulo(txtTitulo.getText());
            filmeSelecionado.setGenero(txtGenero.getText());
            filmeSelecionado.setAno(Integer.parseInt(txtAno.getText()));
            filmeSelecionado.setDiretor(txtDiretor.getText());
            filmeSelecionado.setIdioma(txtIdioma.getText());
            filmeSelecionado.setDisponivel(chkDisponivel.isSelected());

            if (!filmeSelecionado.validarCampos()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios corretamente!\nAno deve estar entre 1900 e 2030.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            filmeSelecionado.alterar();
            JOptionPane.showMessageDialog(this, "Filme alterado com sucesso!");
            limparCampos();
            pesquisarFilmes();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ano inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao alterar filme: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirFilme() {
        if (filmeSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um filme na aba de pesquisa!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int opcao = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir este filme?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (opcao == JOptionPane.YES_OPTION) {
            try {
                filmeSelecionado.excluir();
                JOptionPane.showMessageDialog(this, "Filme excluído com sucesso!");
                limparCampos();
                pesquisarFilmes();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir filme: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void pesquisarFilmes() {
        try {
            String campo = (String) cmbCampoPesquisa.getSelectedItem();
            String texto = txtPesquisa.getText();

            List<Filme> filmes = Filme.pesquisarPorCampo(campo, texto);

            modeloTabela.setRowCount(0);
            for (Filme f : filmes) {
                modeloTabela.addRow(new Object[]{
                        f.getId(),
                        f.getTitulo(),
                        f.getGenero(),
                        f.getAno(),
                        f.getDiretor(),
                        f.getIdioma(),
                        f.isDisponivel() ? "Sim" : "Não"
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao pesquisar filmes: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarFilmeSelecionado() {
        int linhaSelecionada = tabelaPesquisa.getSelectedRow();
        if (linhaSelecionada != -1) {
            filmeSelecionado = new Filme();
            filmeSelecionado.setId((int) modeloTabela.getValueAt(linhaSelecionada, 0));
            filmeSelecionado.setTitulo((String) modeloTabela.getValueAt(linhaSelecionada, 1));
            filmeSelecionado.setGenero((String) modeloTabela.getValueAt(linhaSelecionada, 2));
            filmeSelecionado.setAno((int) modeloTabela.getValueAt(linhaSelecionada, 3));
            filmeSelecionado.setDiretor((String) modeloTabela.getValueAt(linhaSelecionada, 4));
            filmeSelecionado.setIdioma((String) modeloTabela.getValueAt(linhaSelecionada, 5));
            filmeSelecionado.setDisponivel(modeloTabela.getValueAt(linhaSelecionada, 6).equals("Sim"));

            txtId.setText(String.valueOf(filmeSelecionado.getId()));
            txtTitulo.setText(filmeSelecionado.getTitulo());
            txtGenero.setText(filmeSelecionado.getGenero());
            txtAno.setText(String.valueOf(filmeSelecionado.getAno()));
            txtDiretor.setText(filmeSelecionado.getDiretor());
            txtIdioma.setText(filmeSelecionado.getIdioma());
            chkDisponivel.setSelected(filmeSelecionado.isDisponivel());

            abas.setSelectedIndex(0);
        }
    }

    private void limparCampos() {
        txtId.setText("");
        txtTitulo.setText("");
        txtGenero.setText("");
        txtAno.setText("");
        txtDiretor.setText("");
        txtIdioma.setText("");
        chkDisponivel.setSelected(true);
        filmeSelecionado = null;
    }
}
