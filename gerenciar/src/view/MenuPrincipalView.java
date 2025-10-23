package gerenciadorbiblioteca.view;

import javax.swing.*;
import java.awt.*;
import gerenciadorbiblioteca.controller.BibliotecaController;

public class MenuPrincipalView extends JFrame {

    private BibliotecaController controller;

    public MenuPrincipalView(BibliotecaController controller) {
        super("Sistema de Gerenciamento de Biblioteca");
        this.controller = controller;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLayout(new GridLayout(0, 1));

        JButton btnCadastrarLivro = new JButton("1. Cadastrar Livro");
        JButton btnCadastrarUsuario = new JButton("2. Cadastrar Usuário");
        JButton btnSair = new JButton("0. Sair");

        btnCadastrarLivro.addActionListener(e -> controller.abrirTelaCadastroLivro());
        btnCadastrarUsuario.addActionListener(e -> controller.abrirTelaCadastroUsuario());
        btnSair.addActionListener(e -> System.exit(0));

        add(btnCadastrarLivro);
        add(btnCadastrarUsuario);
        add(btnSair);

        setLocationRelativeTo(null);
    }
}
