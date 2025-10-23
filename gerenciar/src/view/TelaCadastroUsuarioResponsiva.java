import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

public class TelaCadastroUsuarioResponsiva extends JFrame {

    // --- Cores e Estilos ---
    private static final Color COR_FUNDO = new Color(240, 240, 240); 
    private static final Color COR_PAINEL = Color.WHITE;
    private static final Color COR_BOTAO_PRIMARIO = new Color(46, 204, 113); 
    private static final Color COR_BOTAO_SECUNDARIO = new Color(230, 126, 34); 
    private static final Font FONT_TITULO = new Font("Segoe UI", Font.BOLD, 20);
    private static final Font FONT_LABEL = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font FONT_BOTAO = new Font("Segoe UI", Font.BOLD, 14);

    // Elementos da Tela
    private JTextField txtNome, txtCPF, txtTelefone, txtGmail;
    private JTextArea txtEndereco; // Alterado para JTextArea
    private JComboBox<String> cmbStatus;
    private JButton btnSalvar, btnLimpar;

    public TelaCadastroUsuarioResponsiva() {
        // Configurar o Look and Feel (Nimbus)
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Fallback para o LAF padrão
        }

        // Configurações Básicas da Janela
        setTitle("✨ Cadastro de Leitores - Versão Responsiva ✨");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(550, 550); // Aumentamos o tamanho para acomodar melhor
        setLocationRelativeTo(null); 
        getContentPane().setBackground(COR_FUNDO);
        
        // --- 1. Inicialização dos Componentes de Entrada ---
        txtNome = createStyledTextField();
        txtCPF = createStyledTextField();
        txtTelefone = createStyledTextField();
        txtGmail = createStyledTextField();
        
        // Endereço como JTextArea com ScrollPane
        txtEndereco = createStyledTextArea();
        JScrollPane scrollEndereco = new JScrollPane(txtEndereco);
        scrollEndereco.setPreferredSize(new Dimension(200, 70)); // Tamanho inicial maior
        
        String[] opcoesStatus = {"Ativo", "Bloqueado"};
        cmbStatus = new JComboBox<>(opcoesStatus);
        cmbStatus.setFont(FONT_LABEL.deriveFont(Font.PLAIN));

        btnSalvar = createStyledButton("Salvar Cadastro", COR_BOTAO_PRIMARIO);
        btnLimpar = createStyledButton("Limpar Campos", COR_BOTAO_SECUNDARIO);

        // --- 2. Painel de Campos (Usando GridBagLayout para Responsividade) ---
        JPanel painelCampos = new JPanel(new GridBagLayout());
        painelCampos.setBackground(COR_PAINEL);
        painelCampos.setBorder(createStyledTitledBorder("Dados do Leitor"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Margem interna
        gbc.fill = GridBagConstraints.HORIZONTAL; // Estica horizontalmente
        
        // Função para adicionar rótulo e campo
        int row = 0;
        row = addField(painelCampos, gbc, "Nome:", txtNome, row);
        row = addField(painelCampos, gbc, "CPF:", txtCPF, row);
        
        // Campo Endereço (JTextArea - ocupa mais espaço)
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.NORTHWEST; // Alinha o rótulo ao topo
        painelCampos.add(createStyledLabel("Endereço:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0; // Garante que o campo de texto se estique horizontalmente
        gbc.weighty = 0.5; // Garante que o campo de endereço se estique verticalmente
        gbc.fill = GridBagConstraints.BOTH; // Estica nas duas direções
        painelCampos.add(scrollEndereco, gbc);
        row++;
        
        // Resetar configurações de peso vertical para campos menores
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        row = addField(painelCampos, gbc, "Telefone:", txtTelefone, row);
        row = addField(painelCampos, gbc, "Gmail (E-mail):", txtGmail, row);
        
        // Status
        gbc.gridx = 0;
        gbc.gridy = row;
        painelCampos.add(createStyledLabel("Status:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        painelCampos.add(cmbStatus, gbc);
        
        // --- 3. Painel de Título e Botões ---
        JLabel titulo = new JLabel("Formulário de Cadastro");
        titulo.setFont(FONT_TITULO);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 15));
        painelBotoes.setBackground(COR_FUNDO);
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnLimpar);
        
        // --- 4. Organizando no JFrame ---
        setLayout(new BorderLayout(15, 15)); 
        add(titulo, BorderLayout.NORTH);
        add(painelCampos, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);

        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        setVisible(true);
        
        // --- 5. Configuração de Eventos ---
        setupActionListeners();
    }

    // --- Métodos de Estilização e Auxiliares ---

    private int addField(JPanel panel, GridBagConstraints gbc, String labelText, JComponent component, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.0; // Rótulos não esticam
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(createStyledLabel(labelText), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0; // Campos de texto esticam
        gbc.anchor = GridBagConstraints.CENTER;
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
        field.setBorder(BorderFactory.createCompoundBorder(
            field.getBorder(), 
            BorderFactory.createEmptyBorder(10, 10, 10, 10) // Padding interno maior
        ));
        return field;
    }
    
    // Método para criar JTextArea estilizado
    private JTextArea createStyledTextArea() {
        JTextArea area = new JTextArea(5, 20); // 3 linhas visíveis inicialmente
        area.setFont(FONT_LABEL.deriveFont(Font.PLAIN));
        area.setLineWrap(true); // Quebra de linha
        area.setWrapStyleWord(true);
        // Não aplica borda de JTextFied, o JScrollPane cuida da borda
        return area;
    }

    private TitledBorder createStyledTitledBorder(String title) {
        return BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            title,
            TitledBorder.LEFT,
            TitledBorder.TOP,
            FONT_LABEL.deriveFont(Font.BOLD, 15), 
            Color.DARK_GRAY
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
    
    private void setupActionListeners() {
        btnSalvar.addActionListener(e -> {
            // Lógica de Validação e Persistência de Dados (BD, etc.)
            String nome = txtNome.getText();
            String endereco = txtEndereco.getText();
            String status = (String) cmbStatus.getSelectedItem();
            
            JOptionPane.showMessageDialog(this, 
                "Dados a Salvar:\n" + 
                "Nome: " + nome + "\n" +
                "Endereço: " + endereco.substring(0, Math.min(endereco.length(), 40)) + "...\n" +
                "Status: " + status, 
                "Sucesso", 
                JOptionPane.INFORMATION_MESSAGE);
        });

        btnLimpar.addActionListener(e -> {
            txtNome.setText("");
            txtCPF.setText("");
            txtEndereco.setText("");
            txtTelefone.setText("");
            txtGmail.setText("");
            cmbStatus.setSelectedIndex(0); 
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaCadastroUsuarioResponsiva());
    }
}