import javax.swing.*;
import java.awt.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Vector;

public class TelaControleDevolucao extends JFrame {

    // --- Configurações Visuais ---
    private static final Color COR_FUNDO = new Color(240, 240, 240); 
    private static final Color COR_PAINEL = Color.WHITE;
    private static final Color COR_AZUL_SISTEMA = new Color(30, 144, 255); 
    private static final Font FONT_TITULO = new Font("Arial", Font.BOLD, 22);
    private static final Font FONT_LABEL = new Font("Arial", Font.BOLD, 13);
    private static final Font FONT_BOTAO = new Font("Arial", Font.BOLD, 14);

    // Elementos da Tela
    private JTextField txtBusca;
    private JButton btnBuscar;
    private JTable tabelaEmprestimos;
    private DefaultTableModel modeloTabela;
    
    private JLabel lblMultaValor, lblMultaDias;
    private JButton btnProcessarDevolucao, btnRegistrarPagamento, btnVoltar;

    // Métricas
    private final double VALOR_MULTA_DIARIA = 1.00; // RN05
    
    public TelaControleDevolucao() {
        // --- Configurações Básicas da Janela ---
        setTitle("⬅️ Controle de Devolução e Multas (RF04, RF10)");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 700); 
        setLocationRelativeTo(null); 
        getContentPane().setBackground(COR_FUNDO);
        setLayout(new BorderLayout(15, 15));

        // --- Título Principal ---
        JLabel titulo = new JLabel("REGISTRO DE DEVOLUÇÃO E PROCESSAMENTO DE MULTAS");
        titulo.setFont(FONT_TITULO.deriveFont(Font.BOLD, 24f));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setBorder(new EmptyBorder(15, 0, 15, 0));
        add(titulo, BorderLayout.NORTH);

        // --- Painel Central (Busca e Tabela) ---
        JPanel painelCentral = new JPanel(new BorderLayout(10, 10));
        painelCentral.setBorder(new EmptyBorder(0, 20, 10, 20));
        painelCentral.setBackground(COR_FUNDO);
        
        painelCentral.add(createSearchPanel(), BorderLayout.NORTH);
        painelCentral.add(createTablePanel(), BorderLayout.CENTER);

        add(painelCentral, BorderLayout.CENTER);

        // --- Painel Inferior (Multa e Ações) ---
        add(createBottomPanel(), BorderLayout.SOUTH);

        setupActionListeners();
        setVisible(true);
    }
    
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setBackground(COR_FUNDO);
        
        txtBusca = new JTextField(25);
        txtBusca.setFont(FONT_LABEL.deriveFont(Font.PLAIN));
        btnBuscar = createButton("Buscar Empréstimos Ativos (ISBN ou CPF)", COR_AZUL_SISTEMA);
        
        panel.add(createLabel("Buscar:"));
        panel.add(txtBusca);
        panel.add(btnBuscar);
        
        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COR_PAINEL);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            "Empréstimos Ativos",
            TitledBorder.CENTER, TitledBorder.TOP, FONT_TITULO.deriveFont(Font.BOLD, 15f)
        ));

        String[] colunas = {"ID Empréstimo", "CPF Leitor", "Nome Leitor", "ISBN Livro", "Título Livro", "Data Empréstimo", "Data Devolução Prevista"};
        
        // Simulação de dados
        String dataAtrasada = LocalDate.now().minusDays(5).toString();
        String dataNoPrazo = LocalDate.now().plusDays(10).toString();
        
        Object[][] dados = {
            {"101", "123.456.789-00", "Ana Silva", "978-8575225968", "Fundamentos de Java", "2025-10-10", dataAtrasada},
            {"102", "111.222.333-44", "Carlos Souza", "978-000", "Livro de Teste Disponível", "2025-10-20", dataNoPrazo}
        };
        
        modeloTabela = new DefaultTableModel(dados, colunas);
        tabelaEmprestimos = new JTable(modeloTabela);
        tabelaEmprestimos.setFont(FONT_LABEL.deriveFont(Font.PLAIN));
        tabelaEmprestimos.getTableHeader().setFont(FONT_LABEL.deriveFont(Font.BOLD));
        tabelaEmprestimos.setRowHeight(25);
        tabelaEmprestimos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 

        // Listener para cálculo de multa ao selecionar linha
        tabelaEmprestimos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabelaEmprestimos.getSelectedRow() != -1) {
                calcularMulta(tabelaEmprestimos.getSelectedRow());
            } else {
                clearMultaInfo();
            }
        });

        panel.add(new JScrollPane(tabelaEmprestimos), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel painelAcoes = new JPanel(new GridBagLayout());
        painelAcoes.setBackground(COR_FUNDO);
        painelAcoes.setBorder(new EmptyBorder(10, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Multa Info
        JPanel painelMulta = new JPanel(new GridLayout(2, 2, 5, 5));
        painelMulta.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.RED), "Cálculo de Multa (RN05)", TitledBorder.LEFT, TitledBorder.TOP, FONT_LABEL, Color.RED));
        
        lblMultaDias = createLabelInfo("Dias de Atraso: 0");
        lblMultaValor = createLabelInfo("Valor Total: R$ 0,00");
        
        painelMulta.add(lblMultaDias);
        painelMulta.add(lblMultaValor);
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridheight = 2; gbc.weightx = 0.5;
        painelAcoes.add(painelMulta, gbc);
        
        // Botões
        btnProcessarDevolucao = createButton("CONFIRMAR DEVOLUÇÃO (RF04)", new Color(46, 204, 113));
        btnRegistrarPagamento = createButton("REGISTRAR PAGAMENTO MULTA (RF11)", new Color(255, 165, 0));
        btnVoltar = createButton("Voltar ao Dashboard", Color.GRAY);
        
        btnRegistrarPagamento.setEnabled(false);
        
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridheight = 1; gbc.weightx = 0.25;
        painelAcoes.add(btnProcessarDevolucao, gbc);
        
        gbc.gridx = 2; gbc.gridy = 0; gbc.gridheight = 1; gbc.weightx = 0.25;
        painelAcoes.add(btnRegistrarPagamento, gbc);
        
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2;
        painelAcoes.add(btnVoltar, gbc);

        return painelAcoes;
    }

    // --- Lógica de Ação ---
    
    private void setupActionListeners() {
        btnBuscar.addActionListener(e -> simularBusca());
        btnProcessarDevolucao.addActionListener(e -> processarDevolucao());
        btnRegistrarPagamento.addActionListener(e -> registrarPagamento());
        
        btnVoltar.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new TelaPrincipal());
        });
    }

    private void simularBusca() {
        String busca = txtBusca.getText().trim();
        if (busca.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o CPF ou ISBN para buscar empréstimos ativos.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Simulação: Apenas recarrega a tabela simulada. Em um sistema real, faria uma consulta SQL.
        JOptionPane.showMessageDialog(this, "Busca por '" + busca + "' realizada. Resultados na tabela.", "Busca", JOptionPane.INFORMATION_MESSAGE);
        
        tabelaEmprestimos.clearSelection();
        clearMultaInfo();
    }
    
    private void calcularMulta(int linhaSelecionada) {
        String dataPrevistaStr = (String) modeloTabela.getValueAt(linhaSelecionada, 6);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        
        try {
            LocalDate dataPrevista = LocalDate.parse(dataPrevistaStr, formatter);
            LocalDate hoje = LocalDate.now();
            
            long diasAtraso = ChronoUnit.DAYS.between(dataPrevista, hoje);
            
            if (diasAtraso > 0) {
                double valorTotal = diasAtraso * VALOR_MULTA_DIARIA; // RF10, RN05
                
                lblMultaDias.setText("Dias de Atraso: " + diasAtraso);
                lblMultaValor.setText(String.format("Valor Total: R$ %.2f", valorTotal));
                
                // Habilita a ação de pagamento e bloqueio (RN06)
                btnRegistrarPagamento.setEnabled(true); 
                btnProcessarDevolucao.setText("CONFIRMAR DEVOLUÇÃO (COM MULTA)");
                btnProcessarDevolucao.setBackground(Color.RED.darker());
            } else {
                // Devolução no prazo ou antecipada
                lblMultaDias.setText("Dias de Atraso: 0");
                lblMultaValor.setText("Valor Total: R$ 0,00");
                btnRegistrarPagamento.setEnabled(false);
                btnProcessarDevolucao.setText("CONFIRMAR DEVOLUÇÃO (NO PRAZO)");
                btnProcessarDevolucao.setBackground(new Color(46, 204, 113));
            }
        } catch (Exception e) {
            System.err.println("Erro ao calcular multa: " + e.getMessage());
            clearMultaInfo();
        }
    }
    
    private void clearMultaInfo() {
        lblMultaDias.setText("Dias de Atraso: 0");
        lblMultaValor.setText("Valor Total: R$ 0,00");
        btnRegistrarPagamento.setEnabled(false);
        btnProcessarDevolucao.setText("CONFIRMAR DEVOLUÇÃO (RF04)");
        btnProcessarDevolucao.setBackground(new Color(46, 204, 113));
    }
    
    private void processarDevolucao() {
        int linhaSelecionada = tabelaEmprestimos.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um empréstimo na tabela.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Verifica se o pagamento de multa é necessário
        if (btnRegistrarPagamento.isEnabled()) {
            JOptionPane.showMessageDialog(this, "Atenção! Multa pendente. Registre o pagamento antes de confirmar a devolução e liberar o leitor.", "Bloqueio (RN06)", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idEmprestimo = (String) modeloTabela.getValueAt(linhaSelecionada, 0);
        String titulo = (String) modeloTabela.getValueAt(linhaSelecionada, 4);
        
        // Simulação da Devolução (RF04, RF05, RN08)
        modeloTabela.removeRow(linhaSelecionada);
        clearMultaInfo();
        
        JOptionPane.showMessageDialog(this, 
            "Devolução do livro '" + titulo + "' (ID: " + idEmprestimo + ") registrada com sucesso!\nStatus do Livro atualizado para 'Disponível'.", 
            "Devolução Concluída", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void registrarPagamento() {
        int linhaSelecionada = tabelaEmprestimos.getSelectedRow();
        if (linhaSelecionada == -1) return;
        
        String valor = lblMultaValor.getText().split(": ")[1];
        
        JOptionPane.showMessageDialog(this, 
            "Pagamento de " + valor + " registrado com sucesso (RF11).\nLeitor liberado para novos empréstimos (RN06).", 
            "Pagamento de Multa", JOptionPane.INFORMATION_MESSAGE);
            
        // Simulação de liberação após pagamento (RF11)
        btnRegistrarPagamento.setEnabled(false);
        // Agora o bibliotecário pode clicar em "Confirmar Devolução"
        btnProcessarDevolucao.setText("CONFIRMAR DEVOLUÇÃO (PAGAMENTO EFETUADO)");
        btnProcessarDevolucao.setBackground(new Color(46, 204, 113));
    }

    // --- Métodos Auxiliares de Estilização ---

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_LABEL);
        label.setForeground(Color.BLACK);
        return label;
    }
    
    private JLabel createLabelInfo(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_LABEL.deriveFont(Font.PLAIN, 14f));
        label.setForeground(Color.DARK_GRAY);
        return label;
    }

    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(FONT_BOTAO);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); 
        return button;
    }
}