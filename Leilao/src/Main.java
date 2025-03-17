import Controller.ClienteController;
import View.MenuClienteView;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        ClienteController controller = new ClienteController();
        MenuClienteView view = new MenuClienteView(controller);
        view.menu();
    }
}