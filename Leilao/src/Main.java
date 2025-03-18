import Controller.ClienteController;
import Controller.LeilaoController;
import Data.ClienteData;
import Data.LeilaoData;
import Model.Cliente;
import Model.Leilao;
import View.MenuPrincipalView;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        ClienteController clienteController = new ClienteController();
        LeilaoController leilaoController = new LeilaoController();

        ClienteData clienteData = new ClienteData();
        List<Cliente> clientes = clienteData.carregarClientes();
        clienteController.setClientes(clientes);

        LeilaoData leilaoData = new LeilaoData();
        List<Leilao> leiloes = leilaoData.carregarLeiloes();
        leilaoController.setLeiloes(leiloes);

        MenuPrincipalView menuPrincipal = new MenuPrincipalView(clienteController, leilaoController);
        menuPrincipal.exibirMenu();

    }
}