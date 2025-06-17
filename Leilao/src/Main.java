import Controller.ClienteController;
import Controller.LeilaoController;
import Controller.NotificacaoController;
import Data.ClienteData;
import Data.LeilaoData;
import Data.LanceData;
import Model.Cliente;
import Model.Lance;
import Model.Leilao;
import View.MenuPrincipalView;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        ClienteController clienteController = new ClienteController();
        LeilaoController leilaoController = new LeilaoController();
        NotificacaoController notificacaoController = new NotificacaoController();

        ClienteData clienteData = new ClienteData();
        List<Cliente> clientes = clienteData.carregarClientes();
        clienteController.setClientes(clientes);

        LeilaoData leilaoData = new LeilaoData();
        List<Leilao> leiloes = leilaoData.carregarLeiloes(clientes, new ArrayList<>());
        leilaoController.setLeiloes(leiloes);


        LanceData lanceData = new LanceData();
        List<Lance> lances = lanceData.carregarLances(clientes, leiloes);
        leiloes = leilaoData.carregarLeiloes(clientes, lances);
        leilaoController.setLeiloes(leiloes);


        leilaoController.verificarStatusLeiloes(leiloes);


        for (Cliente cliente : clientes) {
            notificacaoController.verificarEEnviarEmailInatividade(
                    cliente.getEmail(),
                    cliente.getNome(),
                    cliente.getUltimoLogin()
            );
        }
        NotificacaoController.verificarClientesSemSaldoEEnviarEmail();

        MenuPrincipalView menuPrincipal = new MenuPrincipalView(clienteController, leilaoController);
        menuPrincipal.exibirMenu();
    }
}
