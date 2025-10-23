package gerenciadorbiblioteca.controller;

import gerenciadorbiblioteca.view.MenuPrincipalView;

public class BibliotecaController {

    private MenuPrincipalView menuPrincipalView;

    public BibliotecaController() {
        // Construtor padrão
    }

    public void iniciarAplicacao() {
        System.out.println("Aplicação GUI iniciada.");
        menuPrincipalView = new MenuPrincipalView(this);
    }

    public void abrirTelaCadastroLivro() {
        System.out.println("Abrindo tela de cadastro de livro...");
        // Aqui você poderá abrir uma nova tela (por exemplo, CadastroLivroView)
    }

    public void abrirTelaCadastroUsuario() {
        System.out.println("Abrindo tela de cadastro de usuário...");
        // Aqui você poderá abrir uma nova tela (por exemplo, CadastroUsuarioView)
    }
}
