import javax.swing.*;
import java.awt.*;
import javax.swing.border.TitledBorder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TelaControleEmprestimo extends JFrame {

    // --- Configurações Visuais ---
    private static final Color COR_FUNDO = new Color(240, 240, 240); 
    private static final Color COR_PAINEL = Color.WHITE;
    private static final Color COR_AZUL_SISTEMA = new Color(30, 144, 255); 
    private static final Font FONT_TITULO = new Font("Arial", Font.BOLD, 22);
    private static final Font FONT_LABEL = new Font("Arial", Font.BOLD, 13);
    private static final Font FONT_INFO = new Font("Arial", Font.PLAIN, 14);

    // Campos de Busca
    private JTextField txtBuscaCPF, txtBuscaISBN;
    private JButton btnBuscarLeitor, btnBuscarLivro;
    
    // Painéis de Informação
    private JLabel lblNomeLeitor, lblStatusLeitor, lblLivrosEmprestados, lblTituloLivro, lblStatusLivro, lblDataDevolucao;
    
    // Botões de Ação
    private JButton btnConfirmarEmprestimo, btnVoltar;
    
    // Variáveis de Simulação (Para RN01 e RN04)
    private boolean leitorAtivo = true;
    private boolean possuiMulta = false;
    private int livrosAtuais = 1; // Leitor possui 1 de 3 livros emprestados (RN01)

    public TelaControleEmprestimo() {
        // --- Configurações Básicas da Janela ---
        setTitle("🔄 Controle de Empréstimo de Livros (RF03)");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(850, 650); 
        setLocationRelativeTo(null); 
        getContentPane().setBackground(COR_FUNDO);
        setLayout(new BorderLayout(15, 15));

        // --- Título Principal ---
        JLabel titulo = new JLabel("REGISTRO DE EMPRÉSTIMO");
        titulo.setFont(FONT_TITULO.deriveFont(Font.BOLD, 28f));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setBorder(new EmptyBorder(15, 0, 15, 0));
        add(titulo, BorderLayout.NORTH);

        // --- Painel Central (Dividido em Leitor, Livro e Ação) ---
        JPanel painelCentral = new JPanel(new GridLayout(1, 2, 20, 20));
        painelCentral.setBorder(new EmptyBorder(0, 20, 0, 20));
        painelCentral.setBackground(COR_FUNDO);

        painelCentral.add(createLeitorPanel());
        painelCentral.add(createLivroPanel());

        add(painelCentral, BorderLayout.CENTER);

        // --- Painel Inferior de Ação e Status ---
        add(createBottomPanel(), BorderLayout.SOUTH);

        setupActionListeners();
        setVisible(true);
    }
    
    // --- Criação do Painel de Busca do Leitor ---
    private JPanel createLeitorPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COR_PAINEL);
        panel.setBorder(createStyledTitledBorder("1. Busca do Leitor"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        txtBuscaCPF = createTextField(15);
        btnBuscarLeitor = createButton("Buscar Leitor por CPF", COR_AZUL_SISTEMA);
        
        // Linha 1: Busca
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.5;
        panel.add(createLabel("CPF do Leitor:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 1; gbc.weightx = 1.0;
        panel.add(txtBuscaCPF, gbc);
        gbc.gridx = 2; gbc.gridwidth = 1; gbc.weightx = 0.0;
        panel.add(btnBuscarLeitor, gbc);

        // Linha 2 em diante: Informações do Leitor
        int row = 1;
        lblNomeLeitor = createInfoLabel("N/A");
        row = addInfoRow(panel, gbc, "Nome:", lblNomeLeitor, row);

        lblStatusLeitor = createInfoLabel("Aguardando Busca...");
        row = addInfoRow(panel, gbc, "Status (RN04):", lblStatusLeitor, row);
        
        lblLivrosEmprestados = createInfoLabel("0 de 3 Livros (RN01)");
        row = addInfoRow(panel, gbc, "Limite de Empréstimo:", lblLivrosEmprestados, row);

        return panel;
    }

    // --- Criação do Painel de Busca do Livro ---
    private JPanel createLivroPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COR_PAINEL);
        panel.setBorder(createStyledTitledBorder("2. Busca do Livro"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        txtBuscaISBN = createTextField(15);
        btnBuscarLivro = createButton("Buscar Livro por ISBN", COR_AZUL_SISTEMA);
        
        // Linha 1: Busca
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.5;
        panel.add(createLabel("ISBN do Livro:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 1; gbc.weightx = 1.0;
        panel.add(txtBuscaISBN, gbc);
        gbc.gridx = 2; gbc.gridwidth = 1; gbc.weightx = 0.0;
        panel.add(btnBuscarLivro, gbc);

        // Linha 2 em diante: Informações do Livro
        int row = 1;
        lblTituloLivro = createInfoLabel("N/A");
        row = addInfoRow(panel, gbc, "Título:", lblTituloLivro, row);

        lblStatusLivro = createInfoLabel("Aguardando Busca...");
        row = addInfoRow(panel, gbc, "Status (RF05):", lblStatusLivro, row);
        
        lblDataDevolucao = createInfoLabel("Data: " + LocalDate.now().plusDays(15).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " (RN02)");
        row = addInfoRow(panel, gbc, "Prazo Padrão:", lblDataDevolucao, row);

        return panel;
    }
    
    // --- Criação do Painel Inferior ---
    private JPanel createBottomPanel() {
        JPanel painelFundo = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        painelFundo.setBackground(COR_FUNDO);
        
        btnConfirmarEmprestimo = createButton("CONFIRMAR EMPRÉSTIMO (RF03)", new Color(46, 204, 113));
        btnVoltar = createButton("Voltar ao Dashboard", Color.GRAY);
        
        // Inicialmente desabilitado
        btnConfirmarEmprestimo.setEnabled(false); 

        painelFundo.add(btnConfirmarEmprestimo);
        painelFundo.add(btnVoltar);
        
        return painelFundo;
    }

    // --- Lógica de Simulação e Ações ---
    
    private void setupActionListeners() {
        btnBuscarLeitor.addActionListener(e -> simularBuscaLeitor());
        btnBuscarLivro.addActionListener(e -> simularBuscaLivro());
        
        btnConfirmarEmprestimo.addActionListener(e -> realizarEmprestimo());

        btnVoltar.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new TelaPrincipal());
        });
    }
    
    private void simularBuscaLeitor() {
        String cpf = txtBuscaCPF.getText().trim();
        if (cpf.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o CPF para buscar o leitor.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Simulação de Dados
        String nomeSimulado = (cpf.equals("123")) ? "Ana Silva" : "Leitor Desconhecido";
        leitorAtivo = (cpf.equals("123")); // Simula que apenas '123' é ativo
        possuiMulta = (cpf.equals("999")); // Simula que '999' possui multa
        livrosAtuais = (cpf.equals("123")) ? 1 : 0; 
        
        lblNomeLeitor.setText(nomeSimulado);
        
        if (leitorAtivo) {
            String status = "Ativo";
            if (possuiMulta) {
                status = "BLOQUEADO (Multa Pendente!)"; // RN04
                lblStatusLeitor.setForeground(Color.RED);
            } else if (livrosAtuais >= 3) {
                status = "BLOQUEADO (Limite Excedido!)"; // RN01
                lblStatusLeitor.setForeground(Color.RED);
            } else {
                lblStatusLeitor.setForeground(new Color(46, 139, 87)); // Verde
            }
            lblStatusLeitor.setText(status);
            lblLivrosEmprestados.setText(livrosAtuais + " de 3 Livros (RN01)");
            lblLivrosEmprestados.setForeground(Color.BLACK);
        } else {
            lblStatusLeitor.setText("Inativo/Não Encontrado");
            lblStatusLeitor.setForeground(Color.RED);
            lblLivrosEmprestados.setText("N/A");
        }
        
        checkConditions();
    }
    
    private void simularBuscaLivro() {
        String isbn = txtBuscaISBN.getText().trim();
        if (isbn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o ISBN para buscar o livro.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Simulação de Dados
        String tituloSimulado = (isbn.equals("978-000")) ? "Livro de Teste Disponível" : (isbn.equals("978-111") ? "Livro Emprestado" : "Livro Desconhecido");
        String statusSimulado = (isbn.equals("978-000")) ? "DISPONÍVEL" : (isbn.equals("978-111") ? "EMPRESTADO" : "INDISPONÍVEL");
        
        lblTituloLivro.setText(tituloSimulado);
        lblStatusLivro.setText(statusSimulado);
        
        if (statusSimulado.equals("DISPONÍVEL")) {
             lblStatusLivro.setForeground(new Color(46, 139, 87));
        } else {
            lblStatusLivro.setForeground(Color.RED);
        }

        checkConditions();
    }
    
    private void checkConditions() {
        // Verifica RN04: Condição de Empréstimo
        boolean leitorLiberado = leitorAtivo && !possuiMulta && livrosAtuais < 3 && lblNomeLeitor.getText().equals("Ana Silva"); 
        
        // Verifica RF05: Status do Livro
        boolean livroDisponivel = lblStatusLivro.getText().equals("DISPONÍVEL");
        
        if (leitorLiberado && livroDisponivel) {
            btnConfirmarEmprestimo.setEnabled(true);
            btnConfirmarEmprestimo.setText("CONFIRMAR EMPRÉSTIMO - TUDO OK");
        } else {
            btnConfirmarEmprestimo.setEnabled(false);
            btnConfirmarEmprestimo.setText("CONFIRMAR EMPRÉSTIMO (Verifique status)");
        }
    }

    private void realizarEmprestimo() {
        String dataDevolucao = lblDataDevolucao.getText().split(": ")[1].split(" ")[0];
        
        JOptionPane.showMessageDialog(this, 
            "Empréstimo registrado com sucesso!\n" +
            "Leitor: " + lblNomeLeitor.getText() + 
            "\nLivro: " + lblTituloLivro.getText() + 
            "\nData de Devolução Esperada: " + dataDevolucao, 
            "Empréstimo Registrado (RF03)", JOptionPane.INFORMATION_MESSAGE);
            
        // Simulação de atualização de status (RF05/RN08)
        lblStatusLivro.setText("EMPRESTADO"); 
        
        // Simulação de limite de empréstimo (RN01)
        this.livrosAtuais++;
        
        // Volta ao Dashboard
        dispose();
        SwingUtilities.invokeLater(() -> new TelaPrincipal());
    }

    // --- Métodos Auxiliares de Estilização ---

    private TitledBorder createStyledTitledBorder(String title) {
        return BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            title, TitledBorder.LEFT, TitledBorder.TOP,
            FONT_TITULO.deriveFont(Font.BOLD, 16), Color.DARK_GRAY
        );
    }
    
    private int addInfoRow(JPanel panel, GridBagConstraints gbc, String labelText, JLabel infoLabel, int row) {
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1; gbc.weightx = 0.0;
        panel.add(createLabel(labelText), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2; gbc.weightx = 1.0;
        panel.add(infoLabel, gbc);
        return row + 1;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_LABEL);
        label.setForeground(Color.BLACK);
        return label;
    }
    
    private JLabel createInfoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_INFO);
        label.setForeground(Color.BLUE.darker());
        return label;
    }

    private JTextField createTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(FONT_INFO);
        field.setBorder(BorderFactory.createCompoundBorder(field.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        return field;
    }

    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(FONT_LABEL);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); 
        return button;
    }
}