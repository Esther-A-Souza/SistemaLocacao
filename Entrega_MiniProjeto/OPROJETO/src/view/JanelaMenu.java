package view;

import javax.swing.*;
import java.awt.*;

//Janela principal com menu do sistema

public class JanelaMenu extends JFrame {
    private JMenuBar menuBar;

    public JanelaMenu() {
        setTitle("Sistema de Locação de Filmes - Menu Principal");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        // Painel central com informações
        JPanel painelCentral = new JPanel(new BorderLayout());
        JLabel lblBemVindo = new JLabel("Bem-vindo ao Sistema de Locação de Filmes", SwingConstants.CENTER);
        lblBemVindo.setFont(new Font("Arial", Font.BOLD, 24));
        painelCentral.add(lblBemVindo, BorderLayout.CENTER);
        add(painelCentral);

        // Menu bar
        menuBar = new JMenuBar();

        // Menu Cadastros
        JMenu menuCadastros = new JMenu("Cadastros");
        JMenuItem itemClientes = new JMenuItem("Clientes");
        JMenuItem itemFilmes = new JMenuItem("Filmes");

        itemClientes.addActionListener(e -> new JanelaCliente().setVisible(true));
        itemFilmes.addActionListener(e -> new JanelaFilme().setVisible(true));

        menuCadastros.add(itemClientes);
        menuCadastros.add(itemFilmes);

        // Menu Operações
        JMenu menuOperacoes = new JMenu("Operações");
        JMenuItem itemLocacao = new JMenuItem("Realizar Locação");
        JMenuItem itemDevolucao = new JMenuItem("Realizar Devolução");

        itemLocacao.addActionListener(e -> new JanelaLocacao().setVisible(true));
        itemDevolucao.addActionListener(e -> new JanelaDevolucao().setVisible(true));

        menuOperacoes.add(itemLocacao);
        menuOperacoes.add(itemDevolucao);

        // Menu Relatórios
        JMenu menuRelatorios = new JMenu("Relatórios");
        JMenuItem itemListagem = new JMenuItem("Listagem de Dados");

        itemListagem.addActionListener(e -> new JanelaListagem().setVisible(true));

        menuRelatorios.add(itemListagem);

        // Menu Sair
        JMenu menuSair = new JMenu("Sair");
        JMenuItem itemSair = new JMenuItem("Sair do Sistema");
        itemSair.addActionListener(e -> System.exit(0));
        menuSair.add(itemSair);

        menuBar.add(menuCadastros);
        menuBar.add(menuOperacoes);
        menuBar.add(menuRelatorios);
        menuBar.add(menuSair);

        setJMenuBar(menuBar);
    }
}
