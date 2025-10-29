import javax.swing.*;
import java.awt.*;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaCadastroUsuario extends JFrame {

    // --- Cores e Estilos ---
    private static final Color COR_FUNDO = new Color(240, 240, 240); 
    private static final Color COR_PAINEL = Color.WHITE;
    private static final Color COR_AZUL_SISTEMA = new Color(30, 144, 255); 
    private static final Color COR_BOTAO_SECUNDARIO = new Color(149, 165, 166); 
    private static final Font FONT_TITULO = new Font("Arial", Font.BOLD, 20);
    private static final Font FONT_LABEL = new Font("Arial", Font.BOLD, 13);
    private static final Font FONT_BOTAO = new Font("Arial", Font.BOLD, 14);

    // Elementos da Tela
    private JTextField txtNome, txtCPF, txtTelefone, txtGmail;
    private JTextArea txtEndereco; 
    private JComboBox<String> cmbStatus;
    private JButton btnSalvar, btnLimpar, btnVoltar;

    public TelaCadastroUsuario() {
        // --- Configurações Básicas da Janela ---
        setTitle("👤 Cadastro de Leitores e Usuários");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 600); 
        setLocationRelativeTo(null); 
        getContentPane().setBackground(COR_FUNDO);
        
        // --- Inicialização dos Componentes de Entrada ---
        txtNome = createStyledTextField();
        txtCPF = createStyledTextField();
        txtTelefone = createStyledTextField();
        txtGmail = createStyledTextField();
        
        txtEndereco = createStyledTextArea();
        JScrollPane scrollEndereco = new JScrollPane(txtEndereco);
        scrollEndereco.setPreferredSize(new Dimension(250, 80)); 
        
        String[] opcoesStatus = {"Ativo", "Bloqueado"};
        cmbStatus = new JComboBox<>(opcoesStatus);
        cmbStatus.setFont(FONT_LABEL.deriveFont(Font.PLAIN));

        btnSalvar = createStyledButton("Salvar Cadastro", COR_AZUL_SISTEMA);
        btnLimpar = createStyledButton("Limpar Campos", COR_BOTAO_SECUNDARIO);
        btnVoltar = createStyledButton("Voltar ao Dashboard", Color.GRAY);


        // --- Painel de Campos (GridBagLayout para organização) ---
        JPanel painelCampos = new JPanel(new GridBagLayout());
        painelCampos.setBackground(COR_PAINEL);
        painelCampos.setBorder(createStyledTitledBorder("Dados do Leitor"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); 
        gbc.fill = GridBagConstraints.HORIZONTAL; 
        
        int row = 0;
        row = addField(painelCampos, gbc, "Nome Completo:", txtNome, row);
        row = addField(painelCampos, gbc, "CPF (Obrigatório, RN03):", txtCPF, row);
        
        // Campo Endereço
        gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.NORTHWEST;
        painelCampos.add(createStyledLabel("Endereço:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.weighty = 0.5; gbc.fill = GridBagConstraints.BOTH;
        painelCampos.add(scrollEndereco, gbc);
        row++;
        
        gbc.weighty = 0.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        
        row = addField(painelCampos, gbc, "Telefone:", txtTelefone, row);
        row = addField(painelCampos, gbc, "E-mail (Gmail):", txtGmail, row);
        
        gbc.gridx = 0; gbc.gridy = row;
        painelCampos.add(createStyledLabel("Status (Condição de Empréstimo, RN04):"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        painelCampos.add(cmbStatus, gbc);
        
        // --- Organização no JFrame ---
        JLabel titulo = new JLabel("Formulário de Cadastro de Usuário");
        titulo.setFont(FONT_TITULO.deriveFont(Font.BOLD, 24f));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 15));
        painelBotoes.setBackground(COR_FUNDO);
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnLimpar);
        painelBotoes.add(btnVoltar);
        
        setLayout(new BorderLayout(15, 15)); 
        add(titulo, BorderLayout.NORTH);
        add(painelCampos, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);

        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // --- Configuração de Eventos ---
        setupActionListeners();
        
        setVisible(true);
    }

    // --- Métodos de Estilização e Ações ---

    private void setupActionListeners() {
        btnSalvar.addActionListener(e -> {
            // Lógica de validação simples (RN03 - Nome e CPF obrigatórios)
            if (txtNome.getText().trim().isEmpty() || txtCPF.getText().trim().isEmpty()) {
                 JOptionPane.showMessageDialog(this, "Nome e CPF são campos obrigatórios.", "Erro de Validação", JOptionPane.WARNING_MESSAGE);
                 return;
            }
            JOptionPane.showMessageDialog(this, "Usuário " + txtNome.getText() + " cadastrado/atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
        });

        btnLimpar.addActionListener(e -> clearFields());
        
        // AÇÃO: Botão Voltar -> Volta para a Tela Principal
        btnVoltar.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new TelaPrincipal());
        });
    }
    
    private void clearFields() {
        txtNome.setText("");
        txtCPF.setText("");
        txtEndereco.setText("");
        txtTelefone.setText("");
        txtGmail.setText("");
        cmbStatus.setSelectedIndex(0); 
    }

    private int addField(JPanel panel, GridBagConstraints gbc, String labelText, JComponent component, int row) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.0; gbc.anchor = GridBagConstraints.WEST;
        panel.add(createStyledLabel(labelText), gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.CENTER;
        panel.add(component, gbc);
        return row + 1;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_LABEL);
        label.setForeground(Color.BLACK);
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(FONT_LABEL.deriveFont(Font.PLAIN)); 
        field.setBorder(BorderFactory.createCompoundBorder(field.getBorder(), BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        return field;
    }
    
    private JTextArea createStyledTextArea() {
        JTextArea area = new JTextArea(3, 20); 
        area.setFont(FONT_LABEL.deriveFont(Font.PLAIN));
        area.setLineWrap(true); 
        area.setWrapStyleWord(true);
        return area;
    }

    private TitledBorder createStyledTitledBorder(String title) {
        return BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            title, TitledBorder.LEFT, TitledBorder.TOP,
            FONT_LABEL.deriveFont(Font.BOLD, 15), Color.DARK_GRAY
        );
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(FONT_BOTAO);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); 
        return button;
    }
}