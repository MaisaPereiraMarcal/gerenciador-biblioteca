import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaLogin extends JFrame {

    // --- Configurações Visuais ---
    private static final String NOME_SISTEMA = "Biblioteca Osvaldo Cruz";
    private static final Color COR_AZUL_SISTEMA = new Color(30, 144, 255); 
    private static final Color COR_FUNDO = new Color(240, 240, 240);
    private static final Font FONT_TITULO = new Font("Arial", Font.BOLD, 22);
    private static final Font FONT_LABEL = new Font("Arial", Font.BOLD, 14);
    private static final Font FONT_BOTAO = new Font("Arial", Font.BOLD, 14);

    private JTextField txtUsuario;
    private JPasswordField txtSenha;
    private JButton btnEntrar;

    public TelaLogin() {
        // --- Configurações Básicas da Janela ---
        setTitle(NOME_SISTEMA + " - Acesso ao Sistema");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 350); 
        setLocationRelativeTo(null); 
        getContentPane().setBackground(COR_FUNDO);
        setLayout(new BorderLayout());

        // --- Painel Central de Login ---
        JPanel painelLogin = new JPanel();
        painelLogin.setLayout(new BoxLayout(painelLogin, BoxLayout.Y_AXIS));
        painelLogin.setBackground(Color.WHITE);
        painelLogin.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        // Título
        JLabel lblTitulo = new JLabel("ACESSO ADMINISTRATIVO");
        lblTitulo.setFont(FONT_TITULO);
        lblTitulo.setForeground(COR_AZUL_SISTEMA);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelLogin.add(lblTitulo);
        painelLogin.add(Box.createVerticalStrut(20));

        // Campo Usuário
        painelLogin.add(createLabel("Usuário (Login/Matrícula)"));
        txtUsuario = createTextField();
        painelLogin.add(txtUsuario);
        painelLogin.add(Box.createVerticalStrut(10));

        // Campo Senha
        painelLogin.add(createLabel("Senha"));
        txtSenha = createPasswordField();
        painelLogin.add(txtSenha);
        painelLogin.add(Box.createVerticalStrut(25));

        // Botão Entrar
        btnEntrar = createButton("ENTRAR");
        btnEntrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelLogin.add(btnEntrar);

        // --- Layout Principal ---
        JPanel container = new JPanel(new GridBagLayout());
        container.setBackground(COR_FUNDO);
        container.add(painelLogin);
        
        add(container, BorderLayout.CENTER);
        
        // --- Ação do Botão ---
        btnEntrar.addActionListener(e -> attemptLogin());

        setVisible(true);
    }

    private void attemptLogin() {
        String usuario = txtUsuario.getText();
        String senha = new String(txtSenha.getPassword());

        // Simulação de Autenticação (RF03)
        if (usuario.equals("bibliotecario") && senha.equals("admin123")) {
            dispose(); 
            SwingUtilities.invokeLater(() -> new TelaPrincipal()); // Abre o Dashboard
        } else {
            JOptionPane.showMessageDialog(this, 
                "Credenciais inválidas. Tente novamente.", 
                "Erro de Autenticação", 
                JOptionPane.ERROR_MESSAGE);
            txtSenha.setText("");
        }
    }
    
    // --- Métodos de Estilização ---

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_LABEL);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField(15);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        field.setBorder(new LineBorder(Color.GRAY));
        return field;
    }
    
    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField(15);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        field.setBorder(new LineBorder(Color.GRAY));
        return field;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(FONT_BOTAO);
        button.setBackground(COR_AZUL_SISTEMA);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        return button;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Ignora
        }
        SwingUtilities.invokeLater(() -> new TelaLogin());
    }
}