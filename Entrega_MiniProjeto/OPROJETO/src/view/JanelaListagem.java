package view;

import model.Cliente;
import model.Filme;
import model.Locacao;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

/**
 * Janela para listagem de dados com opções de ordenação
 */
public class JanelaListagem extends JFrame {
    private JComboBox<String> cmbTipoListagem;
    private JComboBox<String> cmbOrdenacao;
    private JTable tabelaListagem;
    private DefaultTableModel modeloTabela;
    private JButton btnGerar;
    private JButton btnExportar;

    public JanelaListagem() {
        setTitle("Listagem de Dados");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));

        // Painel superior
        JPanel painelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        painelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        painelSuperior.add(new JLabel("Tipo de Listagem:"));
        cmbTipoListagem = new JComboBox<>(new String[]{"Clientes", "Filmes", "Locações"});
        cmbTipoListagem.addActionListener(e -> atualizarOpcoesOrdenacao());
        painelSuperior.add(cmbTipoListagem);

        painelSuperior.add(new JLabel("Ordenar por:"));
        cmbOrdenacao = new JComboBox<>();
        painelSuperior.add(cmbOrdenacao);

        btnGerar = new JButton("Gerar Listagem");
        btnGerar.addActionListener(e -> gerarListagem());
        painelSuperior.add(btnGerar);


        add(painelSuperior, BorderLayout.NORTH);

        // Tabela
        modeloTabela = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaListagem = new JTable(modeloTabela);
        JScrollPane scrollPane = new JScrollPane(tabelaListagem);
        add(scrollPane, BorderLayout.CENTER);

        // Inicializar opções de ordenação
        atualizarOpcoesOrdenacao();
    }

    private void atualizarOpcoesOrdenacao() {
        cmbOrdenacao.removeAllItems();
        String tipoSelecionado = (String) cmbTipoListagem.getSelectedItem();

        if (tipoSelecionado.equals("Clientes")) {
            cmbOrdenacao.addItem("Nome (A-Z)");
            cmbOrdenacao.addItem("Nome (Z-A)");
            cmbOrdenacao.addItem("CPF");
        } else if (tipoSelecionado.equals("Filmes")) {
            cmbOrdenacao.addItem("Título (A-Z)");
            cmbOrdenacao.addItem("Título (Z-A)");
            cmbOrdenacao.addItem("Ano (Crescente)");
            cmbOrdenacao.addItem("Ano (Decrescente)");
        } else if (tipoSelecionado.equals("Locações")) {
            cmbOrdenacao.addItem("Data Locação (Recente)");
            cmbOrdenacao.addItem("Data Locação (Antiga)");
            cmbOrdenacao.addItem("Status");
        }
    }

    private void gerarListagem() {
        String tipoListagem = (String) cmbTipoListagem.getSelectedItem();
        String ordenacao = (String) cmbOrdenacao.getSelectedItem();

        try {
            if (tipoListagem.equals("Clientes")) {
                listarClientes(ordenacao);
            } else if (tipoListagem.equals("Filmes")) {
                listarFilmes(ordenacao);
            } else if (tipoListagem.equals("Locações")) {
                listarLocacoes(ordenacao);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao gerar listagem: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listarClientes(String ordenacao) throws SQLException {
        List<Cliente> clientes = Cliente.listarTodos();

        // Ordenar
        if (ordenacao.equals("Nome (A-Z)")) {
            clientes.sort(Comparator.comparing(Cliente::getNome));
        } else if (ordenacao.equals("Nome (Z-A)")) {
            clientes.sort(Comparator.comparing(Cliente::getNome).reversed());
        } else if (ordenacao.equals("CPF")) {
            clientes.sort(Comparator.comparing(Cliente::getCpf));
        }

        // Configurar tabela
        String[] colunas = {"ID", "Nome", "CPF", "Telefone", "Email", "Endereço"};
        modeloTabela.setDataVector(new Object[0][0], colunas);

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
    }

    private void listarFilmes(String ordenacao) throws SQLException {
        List<Filme> filmes = Filme.listarTodos();

        // Ordenar
        if (ordenacao.equals("Título (A-Z)")) {
            filmes.sort(Comparator.comparing(Filme::getTitulo));
        } else if (ordenacao.equals("Título (Z-A)")) {
            filmes.sort(Comparator.comparing(Filme::getTitulo).reversed());
        } else if (ordenacao.equals("Ano (Crescente)")) {
            filmes.sort(Comparator.comparing(Filme::getAno));
        } else if (ordenacao.equals("Ano (Decrescente)")) {
            filmes.sort(Comparator.comparing(Filme::getAno).reversed());
        }

        // Configurar tabela
        String[] colunas = {"ID", "Título", "Gênero", "Ano", "Diretor", "Idioma", "Disponível"};
        modeloTabela.setDataVector(new Object[0][0], colunas);

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
    }

    private void listarLocacoes(String ordenacao) throws SQLException {
        List<Locacao> locacoes = Locacao.listarTodas();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Ordenar
        if (ordenacao.equals("Data Locação (Recente)")) {
            locacoes.sort(Comparator.comparing(Locacao::getDataLocacao).reversed());
        } else if (ordenacao.equals("Data Locação (Antiga)")) {
            locacoes.sort(Comparator.comparing(Locacao::getDataLocacao));
        } else if (ordenacao.equals("Status")) {
            locacoes.sort(Comparator.comparing(Locacao::isDevolvido));
        }

        // Configurar tabela
        String[] colunas = {"ID", "Cliente ID", "Filme ID", "Data Locação", "Devolução Prevista", "Devolução Real", "Status"};
        modeloTabela.setDataVector(new Object[0][0], colunas);

        for (Locacao l : locacoes) {
            modeloTabela.addRow(new Object[]{
                    l.getId(),
                    l.getClienteId(),
                    l.getFilmeId(),
                    l.getDataLocacao().format(formatter),
                    l.getDataDevolucaoPrevista().format(formatter),
                    l.getDataDevolucaoReal() != null ? l.getDataDevolucaoReal().format(formatter) : "-",
                    l.isDevolvido() ? "Devolvido" : "Pendente"
            });
        }
    }

}
