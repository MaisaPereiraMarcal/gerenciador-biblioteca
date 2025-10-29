import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

// Certifique-se que estas classes estão nos seus respectivos arquivos .java
// Exemplo: new TelaLogin() irá chamar o código em TelaLogin.java
// Se o compilador der erro aqui, é porque você não tem o arquivo .java correspondente
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaPrincipal extends JFrame {

    // --- Configurações Visuais e Nomes ---
    private static final String NOME_SISTEMA = "Gestão de Biblioteca";
    
    private static final Color COR_FUNDO_JANELA = new Color(245, 245, 250);
    private static final Color COR_PAINEL_BRANCO = Color.WHITE;
    private static final Color COR_AZUL_SISTEMA = new Color(30, 144, 255); 
    private static final Color COR_AZUL_HEADER = new Color(173, 216, 230);
    private static final Font FONT_TITULO = new Font("Arial", Font.BOLD, 28);
    private static final Font FONT_METRICA = new Font("Arial", Font.BOLD, 36);
    private static final Font FONT_LABEL = new Font("Arial", Font.PLAIN, 14);

    // --- Métricas Simuladas ---
    private final int totalLivros = 1250;
    private final int livrosEmprestados = 450;
    private final int livrosAtrasados = 22;

    public TelaPrincipal() {
        // --- Configurações Básicas da Janela ---
        setTitle(NOME_SISTEMA + " - Menu Principal (Dashboard)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700); 
        setLocationRelativeTo(null); 
        getContentPane().setBackground(COR_FUNDO_JANELA);
        setLayout(new BorderLayout()); 

        // --- 1. Menu de Navegação ---
        setJMenuBar(createMenuBar());

        // --- 2. Título do Dashboard ---
        JLabel lblTitulo = new JLabel("Visão Geral das Operações");
        lblTitulo.setFont(FONT_TITULO);
        lblTitulo.setBorder(new EmptyBorder(20, 30, 20, 30));
        add(lblTitulo, BorderLayout.NORTH);

        // --- 3. Widgets (Métricas) no Centro ---
        JPanel painelDashboard = createDashboardPanel();
        add(painelDashboard, BorderLayout.CENTER);
        
        setVisible(true);
    }

    // --- 1. Criação do Menu de Navegação ---

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(COR_AZUL_HEADER);

        // Menu CADASTRO
        JMenu menuCadastro = new JMenu("Cadastro");
        JMenuItem itemCadUsuario = new JMenuItem("Usuários");
        JMenuItem itemCadLivro = new JMenuItem("Livros");
        
        // Menu CONTROLE
        JMenu menuControle = new JMenu("Controle");
        JMenuItem itemEmprestimo = new JMenuItem("Empréstimo");
        JMenuItem itemDevolucao = new JMenuItem("Devolução");
        JMenuItem itemRenovacao = new JMenuItem("Renovação (RF09)"); // Adicionado RF para clareza
        
        // Menu RELATÓRIOS E AÇÕES
        JMenu menuRelatorios = new JMenu("Relatórios");
        JMenu menuAcoes = new JMenu("Ações");
        JMenuItem itemMultas = new JMenuItem("Multas/Pagamentos");
        
        JMenuItem itemSair = new JMenuItem("Sair");

        // Adiciona Itens aos Menus
        menuCadastro.add(itemCadUsuario);
        menuCadastro.add(itemCadLivro);
        
        menuControle.add(itemEmprestimo);
        menuControle.add(itemDevolucao);
        menuControle.add(itemRenovacao);

        menuAcoes.add(itemMultas);

        // Adiciona Menus à Barra
        menuBar.add(menuCadastro);
        menuBar.add(menuControle);
        menuBar.add(menuRelatorios);
        menuBar.add(menuAcoes);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(itemSair); 

        // --- LÓGICA DE NAVEGAÇÃO COMPLETA (CORRETA) ---
        
        // Ação: Sair -> Volta para a tela de Login
        itemSair.addActionListener(e -> {
            this.dispose();
            // A CLASSE TelaLogin PRECISA ESTAR NO DIRETÓRIO
            SwingUtilities.invokeLater(() -> new TelaLogin()); 
        });

        // Ação: Cadastro de Usuários (RF01)
        itemCadUsuario.addActionListener(e -> navigateTo(new TelaCadastroUsuario()));

        // Ação: Cadastro de Livros (RF02)
        itemCadLivro.addActionListener(e -> navigateTo(new TelaGestaoAcervo()));
        
        // Ação: Controle de Empréstimo (RF03)
        itemEmprestimo.addActionListener(e -> navigateTo(new TelaControleEmprestimo()));
        
        // Ação: Controle de Devolução (RF04)
        itemDevolucao.addActionListener(e -> navigateTo(new TelaControleDevolucao()));

        // Ações Placeholder
        itemRenovacao.addActionListener(e -> JOptionPane.showMessageDialog(this, "Ação futura: Abrir tela de Renovação de Empréstimo (RF09, RN07).", "Em Desenvolvimento", JOptionPane.INFORMATION_MESSAGE));
        itemMultas.addActionListener(e -> JOptionPane.showMessageDialog(this, "Ação futura: Abrir painel de gestão de Multas/Pagamentos (RF11).", "Em Desenvolvimento", JOptionPane.INFORMATION_MESSAGE));
        menuRelatorios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JOptionPane.showMessageDialog(null, "Ação futura: Abrir tela de Geração de Relatórios (RF06, RF07).", "Em Desenvolvimento", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        return menuBar;
    }
    
    // Método auxiliar para navegação
    private void navigateTo(JFrame targetFrame) {
        this.dispose(); // Fecha o Dashboard
        SwingUtilities.invokeLater(() -> targetFrame.setVisible(true)); // Abre a nova tela
    }

    // --- 2. Criação dos Widgets (Métricas) ---
    // (Métodos createDashboardPanel e createMetricWidget são os mesmos e estão corretos)
    
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 30, 30));
        panel.setBackground(COR_FUNDO_JANELA);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Widget 1: Total de Livros
        panel.add(createMetricWidget(
            "Total de Livros Cadastrados",
            String.valueOf(totalLivros),
            COR_AZUL_SISTEMA.darker()
        ));

        // Widget 2: Livros Emprestados
        panel.add(createMetricWidget(
            "Livros Emprestados Atualmente",
            String.valueOf(livrosEmprestados),
            new Color(255, 165, 0)
        ));

        // Widget 3: Alerta de Atrasados
        panel.add(createMetricWidget(
            "Alerta de Livros Atrasados",
            String.valueOf(livrosAtrasados),
            new Color(192, 57, 43)
        ));

        return panel;
    }

    private JPanel createMetricWidget(String title, String value, Color color) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(COR_PAINEL_BRANCO);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Color.LIGHT_GRAY, 1),
            new EmptyBorder(25, 20, 25, 20)
        ));
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(FONT_LABEL);
        lblTitle.setForeground(Color.DARK_GRAY);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(FONT_METRICA);
        lblValue.setForeground(color);
        lblValue.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(lblTitle);
        panel.add(Box.createVerticalStrut(20));
        panel.add(lblValue);

        return panel;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Ignora
        }
        
        // Na execução real, você iniciaria pela TelaLogin
        SwingUtilities.invokeLater(() -> new TelaPrincipal());
    }
}