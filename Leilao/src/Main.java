import Controller.ClienteController;
import Controller.LeilaoController;
import Data.ClienteData;
import Data.LeilaoData;
import Data.LanceData;
import Model.Cliente;
import Model.Leilao;
import Model.Lance;
import View.MenuPrincipalView;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        ClienteController clienteController = new ClienteController();
        LeilaoController leilaoController = new LeilaoController();


        ClienteData clienteData = new ClienteData();
        List<Cliente> clientes = clienteData.carregarClientes();
        clienteController.setClientes(clientes);

        LeilaoData leilaoData = new LeilaoData();
        List<Leilao> leiloes = leilaoData.carregarLeiloes(clientes, new ArrayList<>()); // Passa lista vazia de lances inicialmente
        leilaoController.setLeiloes(leiloes);


        LanceData lanceData = new LanceData();
        List<Lance> lances = lanceData.carregarLances(clientes, leiloes);

        leiloes = leilaoData.carregarLeiloes(clientes, lances);
        leilaoController.setLeiloes(leiloes);


        MenuPrincipalView menuPrincipal = new MenuPrincipalView(clienteController, leilaoController);
        menuPrincipal.exibirMenu();
    }
}