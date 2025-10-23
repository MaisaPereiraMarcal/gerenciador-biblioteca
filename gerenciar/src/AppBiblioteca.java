
package gerenciadorbiblioteca;


import javax.swing.SwingUtilities;

import gerenciador.biblioteca.view.MenuPrincipalView;

public class AppBiblioteca {


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuPrincipalView().setVisible(true));
    }


}