package view;

import model.Cliente;
import util.Mensagens;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

//Janela para gerenciamento de clientes com suporte completo a internacionalização

public class JanelaCliente extends JFrame {
    private JTabbedPane abas;
    private JTextField txtId, txtNome, txtCpf, txtTelefone, txtEmail, txtEndereco;
    private JTextField txtPesquisa;
    private JTable tabelaPesquisa;
    private DefaultTableModel modeloTabela;
    private JButton btnIncluir, btnAlterar, btnExcluir, btnLimpar;
    private JButton btnPesquisar;
    private JComboBox<String> cmbIdioma;
    private Cliente clienteSelecionado;

    // Labels que serão traduzidos
    private JLabel lblId, lblNome, lblCpf, lblTelefone, lblEmail, lblEndereco, lblIdioma;
    private JLabel lblPesquisar;

    public JanelaCliente() {
        setTitle("Gerenciamento de Clientes");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        abas = new JTabbedPane();

        // Aba de Cadastro
        JPanel painelCadastro = criarPainelCadastro();
        abas.addTab("Cadastro", painelCadastro);

        // Aba de Pesquisa
        JPanel painelPesquisa = criarPainelPesquisa();
        abas.addTab("Pesquisa", painelPesquisa);

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
        lblId = new JLabel("ID:");
        painelCampos.add(lblId, gbc);
        gbc.gridx = 1;
        txtId = new JTextField(10);
        txtId.setEditable(false);
        painelCampos.add(txtId, gbc);

        // Idioma
        gbc.gridx = 2;
        lblIdioma = new JLabel("Idioma:");
        painelCampos.add(lblIdioma, gbc);
        gbc.gridx = 3;
        cmbIdioma = new JComboBox<>(new String[]{"Português", "English"});
        cmbIdioma.addActionListener(e -> alterarIdioma());
        painelCampos.add(cmbIdioma, gbc);

        // Nome
        gbc.gridx = 0; gbc.gridy = 1;
        lblNome = new JLabel("Nome:*");
        painelCampos.add(lblNome, gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        txtNome = new JTextField(30);
        painelCampos.add(txtNome, gbc);

        // CPF
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        lblCpf = new JLabel("CPF:*");
        painelCampos.add(lblCpf, gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        txtCpf = new JTextField(15);
        painelCampos.add(txtCpf, gbc);

        // Telefone
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1;
        lblTelefone = new JLabel("Telefone:");
        painelCampos.add(lblTelefone, gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        txtTelefone = new JTextField(15);
        painelCampos.add(txtTelefone, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1;
        lblEmail = new JLabel("Email:");
        painelCampos.add(lblEmail, gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        txtEmail = new JTextField(30);
        painelCampos.add(txtEmail, gbc);

        // Endereço
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 1;
        lblEndereco = new JLabel("Endereço:");
        painelCampos.add(lblEndereco, gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        txtEndereco = new JTextField(40);
        painelCampos.add(txtEndereco, gbc);

        painel.add(painelCampos, BorderLayout.NORTH);

        // Painel de botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnIncluir = new JButton("Incluir");
        btnAlterar = new JButton("Alterar");
        btnExcluir = new JButton("Excluir");
        btnLimpar = new JButton("Limpar");

        btnIncluir.addActionListener(e -> incluirCliente());
        btnAlterar.addActionListener(e -> alterarCliente());
        btnExcluir.addActionListener(e -> excluirCliente());
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

        // Painel de pesquisa
        JPanel painelPesquisaTopo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblPesquisar = new JLabel("Pesquisar:");
        painelPesquisaTopo.add(lblPesquisar);
        txtPesquisa = new JTextField(30);
        painelPesquisaTopo.add(txtPesquisa);
        btnPesquisar = new JButton("Pesquisar");
        btnPesquisar.addActionListener(e -> pesquisarClientes());
        painelPesquisaTopo.add(btnPesquisar);

        painel.add(painelPesquisaTopo, BorderLayout.NORTH);

        // Tabela de resultados
        String[] colunas = {"ID", "Nome", "CPF", "Telefone", "Email", "Endereço"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaPesquisa = new JTable(modeloTabela);
        tabelaPesquisa.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabelaPesquisa.getSelectedRow() != -1) {
                carregarClienteSelecionado();
            }
        });

        JScrollPane scrollPane = new JScrollPane(tabelaPesquisa);
        painel.add(scrollPane, BorderLayout.CENTER);

        return painel;
    }

    private void alterarIdioma() {
        String idiomaSelecionado = (String) cmbIdioma.getSelectedItem();
        if (idiomaSelecionado.equals("English")) {
            Mensagens.setIdioma("en");
        } else {
            Mensagens.setIdioma("pt");
        }
        atualizarTextos();
    }

    private void atualizarTextos() {
        String idioma = Mensagens.getLocaleAtual().getLanguage();

        if (idioma.equals("en")) {
            // Janela
            setTitle("Customer Management");

            // Abas
            abas.setTitleAt(0, "Register");
            abas.setTitleAt(1, "Search");

            // Labels
            lblId.setText("ID:");
            lblIdioma.setText("Language:");
            lblNome.setText("Name:*");
            lblCpf.setText("Tax ID:*");
            lblTelefone.setText("Phone:");
            lblEmail.setText("Email:");
            lblEndereco.setText("Address:");
            lblPesquisar.setText("Search:");

            // Botões
            btnIncluir.setText("Add");
            btnAlterar.setText("Update");
            btnExcluir.setText("Delete");
            btnLimpar.setText("Clear");
            btnPesquisar.setText("Search");

            // Colunas da tabela
            modeloTabela.setColumnIdentifiers(new String[]{"ID", "Name", "Tax ID", "Phone", "Email", "Address"});

        } else {
            // Português
            setTitle("Gerenciamento de Clientes");

            abas.setTitleAt(0, "Cadastro");
            abas.setTitleAt(1, "Pesquisa");

            lblId.setText("ID:");
            lblIdioma.setText("Idioma:");
            lblNome.setText("Nome:*");
            lblCpf.setText("CPF:*");
            lblTelefone.setText("Telefone:");
            lblEmail.setText("Email:");
            lblEndereco.setText("Endereço:");
            lblPesquisar.setText("Pesquisar:");

            btnIncluir.setText("Incluir");
            btnAlterar.setText("Alterar");
            btnExcluir.setText("Excluir");
            btnLimpar.setText("Limpar");
            btnPesquisar.setText("Pesquisar");

            modeloTabela.setColumnIdentifiers(new String[]{"ID", "Nome", "CPF", "Telefone", "Email", "Endereço"});
        }
    }

    private void incluirCliente() {
        try {
            Cliente cliente = new Cliente();
            cliente.setNome(txtNome.getText());
            cliente.setCpf(txtCpf.getText());
            cliente.setTelefone(txtTelefone.getText());
            cliente.setEmail(txtEmail.getText());
            cliente.setEndereco(txtEndereco.getText());

            if (!cliente.validarCampos()) {
                String msg = Mensagens.getLocaleAtual().getLanguage().equals("en")
                        ? "Fill in all required fields correctly!"
                        : "Preencha todos os campos obrigatórios corretamente!";
                JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            cliente.incluir();
            String msg = Mensagens.getLocaleAtual().getLanguage().equals("en")
                    ? "Customer added successfully!"
                    : "Cliente incluído com sucesso!";
            JOptionPane.showMessageDialog(this, msg);
            limparCampos();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao incluir cliente: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void alterarCliente() {
        if (clienteSelecionado == null) {
            String msg = Mensagens.getLocaleAtual().getLanguage().equals("en")
                    ? "Select a customer in the search tab!"
                    : "Selecione um cliente na aba de pesquisa!";
            JOptionPane.showMessageDialog(this, msg, "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            clienteSelecionado.setNome(txtNome.getText());
            clienteSelecionado.setCpf(txtCpf.getText());
            clienteSelecionado.setTelefone(txtTelefone.getText());
            clienteSelecionado.setEmail(txtEmail.getText());
            clienteSelecionado.setEndereco(txtEndereco.getText());

            if (!clienteSelecionado.validarCampos()) {
                String msg = Mensagens.getLocaleAtual().getLanguage().equals("en")
                        ? "Fill in all required fields correctly!"
                        : "Preencha todos os campos obrigatórios corretamente!";
                JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            clienteSelecionado.alterar();
            String msg = Mensagens.getLocaleAtual().getLanguage().equals("en")
                    ? "Customer updated successfully!"
                    : "Cliente alterado com sucesso!";
            JOptionPane.showMessageDialog(this, msg);
            limparCampos();
            pesquisarClientes();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao alterar cliente: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirCliente() {
        if (clienteSelecionado == null) {
            String msg = Mensagens.getLocaleAtual().getLanguage().equals("en")
                    ? "Select a customer in the search tab!"
                    : "Selecione um cliente na aba de pesquisa!";
            JOptionPane.showMessageDialog(this, msg, "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String msgConfirm = Mensagens.getLocaleAtual().getLanguage().equals("en")
                ? "Do you really want to delete this customer?"
                : "Deseja realmente excluir este cliente?";
        int opcao = JOptionPane.showConfirmDialog(this, msgConfirm, "Confirmation", JOptionPane.YES_NO_OPTION);
        if (opcao == JOptionPane.YES_OPTION) {
            try {
                clienteSelecionado.excluir();
                String msg = Mensagens.getLocaleAtual().getLanguage().equals("en")
                        ? "Customer deleted successfully!"
                        : "Cliente excluído com sucesso!";
                JOptionPane.showMessageDialog(this, msg);
                limparCampos();
                pesquisarClientes();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir cliente: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void pesquisarClientes() {
        try {
            String textoPesquisa = txtPesquisa.getText();
            List<Cliente> clientes = Cliente.pesquisar(textoPesquisa);

            modeloTabela.setRowCount(0);
            for (Cliente c : clientes) {
                modeloTabela.addRow(new Object[]{
                        c.getId(),
                        c.getNome(),
                        c.getCpf(),
                        c.getTelefone(),
                        c.getEmail(),
                        c.getEndereco()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao pesquisar clientes: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarClienteSelecionado() {
        int linhaSelecionada = tabelaPesquisa.getSelectedRow();
        if (linhaSelecionada != -1) {
            clienteSelecionado = new Cliente();
            clienteSelecionado.setId((int) modeloTabela.getValueAt(linhaSelecionada, 0));
            clienteSelecionado.setNome((String) modeloTabela.getValueAt(linhaSelecionada, 1));
            clienteSelecionado.setCpf((String) modeloTabela.getValueAt(linhaSelecionada, 2));
            clienteSelecionado.setTelefone((String) modeloTabela.getValueAt(linhaSelecionada, 3));
            clienteSelecionado.setEmail((String) modeloTabela.getValueAt(linhaSelecionada, 4));
            clienteSelecionado.setEndereco((String) modeloTabela.getValueAt(linhaSelecionada, 5));

            txtId.setText(String.valueOf(clienteSelecionado.getId()));
            txtNome.setText(clienteSelecionado.getNome());
            txtCpf.setText(clienteSelecionado.getCpf());
            txtTelefone.setText(clienteSelecionado.getTelefone());
            txtEmail.setText(clienteSelecionado.getEmail());
            txtEndereco.setText(clienteSelecionado.getEndereco());

            abas.setSelectedIndex(0);
        }
    }

    private void limparCampos() {
        txtId.setText("");
        txtNome.setText("");
        txtCpf.setText("");
        txtTelefone.setText("");
        txtEmail.setText("");
        txtEndereco.setText("");
        clienteSelecionado = null;
    }
}
