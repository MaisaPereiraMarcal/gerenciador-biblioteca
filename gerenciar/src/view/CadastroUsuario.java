import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CadastroUsuario extends JFrame {

    // Campos de entrada
    private JTextField nomeField;
    private JTextField cpfField;
    private JTextField enderecoField;
    private JTextField telefoneField;
    private JTextField gmailField;
    private JComboBox<String> statusBox;

    public CadastroUsuario() {
        setTitle("Cadastro de Usuário");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // centraliza a janela

        // Layout
        setLayout(new GridLayout(7, 2, 10, 10));

        // Componentes
        add(new JLabel("Nome:"));
        nomeField = new JTextField();
        add(nomeField);

        add(new JLabel("CPF:"));
        cpfField = new JTextField();
        add(cpfField);

        add(new JLabel("Endereço:"));
        enderecoField = new JTextField();
        add(enderecoField);

        add(new JLabel("Telefone:"));
        telefoneField = new JTextField();
        add(telefoneField);

        add(new JLabel("Gmail:"));
        gmailField = new JTextField();
        add(gmailField);

        add(new JLabel("Status do Usuário:"));
        String[] statusOpcoes = {"Ativo", "Bloqueado"};
        statusBox = new JComboBox<>(statusOpcoes);
        add(statusBox);

        JButton cadastrarButton = new JButton("Cadastrar");
        add(cadastrarButton);

        JButton limparButton = new JButton("Limpar");
        add(limparButton);

        // Ações dos botões
        cadastrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cadastrarUsuario();
            }
        });

        limparButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limparCampos();
            }
        });

        setVisible(true);
    }

    private void cadastrarUsuario() {
        String nome = nomeField.getText();
        String cpf = cpfField.getText();
        String endereco = enderecoField.getText();
        String telefone = telefoneField.getText();
        String gmail = gmailField.getText();
        String status = (String) statusBox.getSelectedItem();

        // Só um exemplo, aqui você poderia salvar em um banco de dados
        JOptionPane.showMessageDialog(this,
                "Usuário cadastrado:\n" +
                        "Nome: " + nome + "\n" +
                        "CPF: " + cpf + "\n" +
                        "Endereço: " + endereco + "\n" +
                        "Telefone: " + telefone + "\n" +
                        "Gmail: " + gmail + "\n" +
                        "Status: " + status);
    }

    private void limparCampos() {
        nomeField.setText("");
        cpfField.setText("");
        enderecoField.setText("");
        telefoneField.setText("");
        gmailField.setText("");
        statusBox.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        new CadastroUsuario();
    }
}
