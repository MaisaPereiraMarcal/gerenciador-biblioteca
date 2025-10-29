import javax.swing.SwingUtilities;
import com.meuprojeto.controller.BibliotecaController;
import com.meuprojeto.view.MenuPrincipalView;

public class AppBiblioteca {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BibliotecaController controller = new BibliotecaController();
            new MenuPrincipalView(controller);
        });
    }
}
