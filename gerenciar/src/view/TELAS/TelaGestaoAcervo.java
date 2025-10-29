import javax.swing.*;
import java.awt.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaGestaoAcervo extends JFrame {

    // --- Configurações Visuais e Nomes ---
    private static final String NOME_SISTEMA = "Gestão de Biblioteca";
    
    private static final Color COR_FUNDO_JANELA = new Color(225, 225, 230); 
    private static final Color COR_PAINEL_BRANCO = Color.WHITE;
    private static final Color COR_AZUL_SISTEMA = new Color(30, 144, 255); 
    private static final Color COR_AZUL_HEADER = new Color(173, 216, 230); 
    private static final Color COR_CAMPO_TEXTO = new Color(250, 250, 250);
    private static final Font FONT_TITULO = new Font("Arial", Font.BOLD, 22);
    private static final Font FONT_LABEL = new Font("Arial", Font.BOLD, 12);
    private static final Font FONT_BOTAO = new Font("Arial", Font.BOLD, 12);
    
    private int proximoCodigoLivro = 3; 

    // Elementos da Tela
    private JComboBox<String> cmbArea, cmbLocal; 
    private JTextField txtTitulo, txtAutor, txtEditora, txtAno, txtISBN, txtQtdExemplares;
    private JTextField txtStatus; // Status gerado automaticamente (RF05, RN08)
    private JTable tabelaLivros;
    private DefaultTableModel modeloTabela;
    
    private JButton btnNovo, btnSalvar, btnCancelar, btnEditar, btnExcluir, btnVoltar, btnAtualizar;

    public TelaGestaoAcervo() {
        // --- Configurações Básicas da Janela ---
        setTitle(NOME_SISTEMA + " - Cadastro e Controle de Acervo");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(950, 750); 
        setLocationRelativeTo(null); 
        getContentPane().setBackground(COR_FUNDO_JANELA);
        setLayout(new BorderLayout(5, 5)); 
        
        setJMenuBar(createMenuBar());

        // --- Painel Principal (Container) ---
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS));
        painelPrincipal.setBackground(COR_FUNDO_JANELA);
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Título Principal da Tela
        JLabel lblTitulo = new JLabel(" 📚 CADASTRO E GESTÃO DE ACERVO ");
        lblTitulo.setFont(FONT_TITULO.deriveFont(Font.BOLD, 26f));
        lblTitulo.setForeground(new Color(40, 40, 40)); 
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel painelHeader = new JPanel();
        painelHeader.setBackground(COR_PAINEL_BRANCO);
        painelHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        painelHeader.add(lblTitulo);
        
        painelPrincipal.add(Box.createVerticalStrut(10));
        painelPrincipal.add(painelHeader);
        painelPrincipal.add(Box.createVerticalStrut(25));

        // Painel de Campos de Cadastro
        JPanel painelCadastro = createCadastroPanel();
        painelCadastro.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelPrincipal.add(painelCadastro);
        painelPrincipal.add(Box.createVerticalStrut(25));

        // Painel da Tabela de Livros
        JPanel painelTabela = createTablePanel();
        painelTabela.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelPrincipal.add(painelTabela);
        painelPrincipal.add(Box.createVerticalGlue()); 

        // Painel de Botões Inferior
        JPanel painelBotoes = createBottomButtonPanel();
        painelPrincipal.add(painelBotoes);

        add(painelPrincipal, BorderLayout.CENTER);
        
        setupActionListeners();
        
        setVisible(true);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(COR_AZUL_HEADER);
        
        JMenu menuCadastros = new JMenu("Cadastro");
        JMenu menuControle = new JMenu("Controle");
        JMenu menuRelatorios = new JMenu("Relatórios");
        JMenu menuAcoes = new JMenu("Ações");

        JMenuItem itemCadUsuario = new JMenuItem("Usuários");
        
        // Ação: Cadastro de Usuários -> Abre TelaCadastroUsuario
        itemCadUsuario.addActionListener(e -> {
            this.dispose(); 
            SwingUtilities.invokeLater(() -> new TelaCadastroUsuario());
        });

        menuCadastros.add(itemCadUsuario);
        menuCadastros.add(new JMenuItem("Livros")); // Esta tela
        
        menuBar.add(menuCadastros);
        menuBar.add(menuControle);
        menuBar.add(menuRelatorios);
        menuBar.add(menuAcoes);
        
        return menuBar;
    }

    private JPanel createCadastroPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COR_PAINEL_BRANCO);
        panel.setMaximumSize(new Dimension(850, 300));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Inicialização dos Campos (RF02)
        cmbArea = new JComboBox<>(new String[]{"Ciências Exatas", "Ciências Humanas", "Engenharia", "Artes"});
        cmbLocal = new JComboBox<>(new String[]{"Prateleira A1", "Prateleira B2", "Depósito Principal"});
        
        txtTitulo = createStyledTextField(30);
        txtAutor = createStyledTextField(20);
        txtEditora = createStyledTextField(20);
        txtAno = createStyledTextField(10);
        txtISBN = createStyledTextField(15);
        txtQtdExemplares = createStyledTextField(10);
        
        txtStatus = createStyledTextField(15);
        txtStatus.setEditable(false); 
        txtStatus.setText("Livre"); 
        
        // Linha 1: Título e Autor
        int row = 0;
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1; panel.add(createStyledLabel("Título da Obra"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; panel.add(txtTitulo, gbc);
        gbc.gridx = 3; gbc.gridwidth = 1; panel.add(createStyledLabel("Autor"), gbc);
        gbc.gridx = 4; gbc.gridwidth = 2; panel.add(txtAutor, gbc);
        row++;

        // Linha 2: Editora, Ano e ISBN (RN03 - ISBN obrigatório)
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1; panel.add(createStyledLabel("Editora"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 1; panel.add(txtEditora, gbc);
        gbc.gridx = 2; gbc.gridwidth = 1; panel.add(createStyledLabel("Ano"), gbc);
        gbc.gridx = 3; gbc.gridwidth = 1; panel.add(txtAno, gbc);
        gbc.gridx = 4; gbc.gridwidth = 1; panel.add(createStyledLabel("ISBN (Obrigatório, RN03)"), gbc);
        gbc.gridx = 5; gbc.gridwidth = 1; panel.add(txtISBN, gbc);
        row++;
        
        // Linha 3: Área, Local, Qtd e Status
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1; panel.add(createStyledLabel("Área de Conhecimento"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 1; panel.add(cmbArea, gbc);
        gbc.gridx = 2; gbc.gridwidth = 1; panel.add(createStyledLabel("Localização no Acervo"), gbc);
        gbc.gridx = 3; gbc.gridwidth = 1; panel.add(cmbLocal, gbc);
        gbc.gridx = 4; gbc.gridwidth = 1; panel.add(createStyledLabel("Total de Exemplares"), gbc);
        gbc.gridx = 5; gbc.gridwidth = 1; panel.add(txtQtdExemplares, gbc);
        row++;
        
        // Linha 4: Status (Automático)
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1; panel.add(createStyledLabel("Status Atual (Automático)"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; panel.add(txtStatus, gbc);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COR_PAINEL_BRANCO);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            "Itens Registrados no Acervo",
            TitledBorder.CENTER, TitledBorder.TOP, FONT_TITULO.deriveFont(Font.BOLD, 15f)
        ));

        // Colunas ajustadas para o RF02
        String[] colunas = {"ID", "ISBN", "Título da Obra", "Autor", "Exemplares", "Status"};
        Object[][] dados = {
            {"1", "978-8575225968", "Fundamentos de Java", "Deitel & Deitel", "5", "Livre"},
            {"2", "978-8521630132", "Cálculo Avançado I", "James Stewart", "12", "Em Uso"}
        };
        
        modeloTabela = new DefaultTableModel(dados, colunas);
        tabelaLivros = new JTable(modeloTabela);
        tabelaLivros.setFont(FONT_LABEL.deriveFont(Font.PLAIN));
        tabelaLivros.getTableHeader().setFont(FONT_LABEL.deriveFont(Font.BOLD));
        tabelaLivros.setRowHeight(25);
        tabelaLivros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 

        JScrollPane scrollPane = new JScrollPane(tabelaLivros);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel painelAtualizar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelAtualizar.setBackground(COR_PAINEL_BRANCO);
        btnAtualizar = createIconButton("Atualizar Lista", new Color(192, 57, 43), "🔄"); 
        painelAtualizar.add(btnAtualizar);
        panel.add(painelAtualizar, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createBottomButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 15)); 
        panel.setBackground(COR_FUNDO_JANELA);
        
        btnNovo = createIconButton("Novo Item", new Color(46, 204, 113), "➕");
        btnSalvar = createIconButton("Salvar Registro", COR_AZUL_SISTEMA, "✅");
        btnCancelar = createIconButton("Limpar Campos", new Color(189, 195, 199), "✖");
        btnEditar = createIconButton("Editar Seleção", new Color(255, 165, 0), "✏️");
        btnExcluir = createIconButton("Excluir Item", new Color(192, 57, 43), "🗑️");
        btnVoltar = createIconButton("Voltar ao Dashboard", new Color(149, 165, 166), "🔙");

        panel.add(btnNovo);
        panel.add(btnSalvar);
        panel.add(btnCancelar);
        panel.add(btnEditar);
        panel.add(btnExcluir);
        panel.add(btnVoltar);

        return panel;
    }
    
    // --- Lógica de Ação dos Botões ---

    private void setupActionListeners() {
        
        btnNovo.addActionListener(e -> clearFields());

        btnSalvar.addActionListener(e -> {
            String isbn = txtISBN.getText().trim();
            String titulo = txtTitulo.getText().trim();
            String autor = txtAutor.getText().trim();
            String qtd = txtQtdExemplares.getText().trim();
            
            // Validação de campos obrigatórios (RF02, RN03)
            if (titulo.isEmpty() || isbn.isEmpty() || qtd.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Título, ISBN e Quantidade de Exemplares são obrigatórios.", "Erro", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            try {
                int exemplares = Integer.parseInt(qtd);
                String novoStatus = (exemplares > 0) ? "Livre" : "Esgotado"; // RF05, RN08
                txtStatus.setText(novoStatus);
                
                // Simulação de Salvar
                Object[] novaLinha = {
                    String.valueOf(proximoCodigoLivro),
                    isbn,
                    titulo,
                    autor,
                    qtd,
                    novoStatus
                };
                modeloTabela.addRow(novaLinha);
                proximoCodigoLivro++;
                
                JOptionPane.showMessageDialog(this, "Livro '" + titulo + "' salvo no acervo.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "A Quantidade de Exemplares deve ser um número inteiro válido.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnCancelar.addActionListener(e -> {
            clearFields();
            JOptionPane.showMessageDialog(this, "Campos limpos com sucesso.", "Ação Concluída", JOptionPane.INFORMATION_MESSAGE);
        });
        
        btnEditar.addActionListener(e -> {
            int linhaSelecionada = tabelaLivros.getSelectedRow();
            if (linhaSelecionada == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um item na tabela para editar.", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Carrega os dados da tabela para os campos de entrada (Apenas os visíveis na tabela para simplificação)
            txtISBN.setText((String) modeloTabela.getValueAt(linhaSelecionada, 1));
            txtTitulo.setText((String) modeloTabela.getValueAt(linhaSelecionada, 2));
            txtQtdExemplares.setText((String) modeloTabela.getValueAt(linhaSelecionada, 4));
            txtStatus.setText((String) modeloTabela.getValueAt(linhaSelecionada, 5));
            
            JOptionPane.showMessageDialog(this, "Dados de edição carregados. Altere e clique em 'Salvar'.", "Modo Edição", JOptionPane.INFORMATION_MESSAGE);
        });
        
        btnExcluir.addActionListener(e -> {
            int linhaSelecionada = tabelaLivros.getSelectedRow();
            if (linhaSelecionada == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um item para exclusão.", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String tituloExcluido = (String) modeloTabela.getValueAt(linhaSelecionada, 2);
            
            int confirmacao = JOptionPane.showConfirmDialog(this, 
                "Confirma a exclusão de '" + tituloExcluido + "' do acervo?", 
                "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
                
            if (confirmacao == JOptionPane.YES_OPTION) {
                modeloTabela.removeRow(linhaSelecionada);
                JOptionPane.showMessageDialog(this, "Item '" + tituloExcluido + "' removido do acervo.", "Exclusão", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        btnAtualizar.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Lista de acervo recarregada do sistema (Simulação RF06/RF07).", "Atualização", JOptionPane.INFORMATION_MESSAGE);
        });

        // AÇÃO: Botão Voltar -> Volta para o Dashboard
        btnVoltar.addActionListener(e -> {
            dispose(); 
            SwingUtilities.invokeLater(() -> new TelaPrincipal());
        });
    }
    
    private void clearFields() {
        txtTitulo.setText("");
        txtAutor.setText("");
        txtEditora.setText("");
        txtAno.setText("");
        txtISBN.setText("");
        txtQtdExemplares.setText("");
        txtStatus.setText("Livre");
        cmbArea.setSelectedIndex(0);
        cmbLocal.setSelectedIndex(0);
        tabelaLivros.clearSelection(); 
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_LABEL);
        label.setForeground(new Color(80, 80, 80));
        return label;
    }

    private JTextField createStyledTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(FONT_LABEL.deriveFont(Font.PLAIN)); 
        field.setBackground(COR_CAMPO_TEXTO);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY), 
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return field;
    }

    private JButton createIconButton(String text, Color bgColor, String iconText) {
        JButton button = new JButton(iconText + " " + text);
        button.setFont(FONT_BOTAO);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); 
        
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        return button;
    }
}