import Controller.ClienteController;
import Controller.LeilaoController;
import Controller.NotificacaoController;
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

        // Inicializa os controladores
        ClienteController clienteController = new ClienteController();
        LeilaoController leilaoController = new LeilaoController();
        NotificacaoController notificacaoController = new NotificacaoController();  // Instancia o controller de notificações

        // Carrega os clientes e define no controller
        ClienteData clienteData = new ClienteData();
        List<Cliente> clientes = clienteData.carregarClientes();
        clienteController.setClientes(clientes);

        // Carrega os leilões com lista vazia de lances e define no controller
        LeilaoData leilaoData = new LeilaoData();
        List<Leilao> leiloes = leilaoData.carregarLeiloes(clientes, new ArrayList<>());
        leilaoController.setLeiloes(leiloes);
        leilaoController.verificarStatusLeiloes(leiloes);

        // Carrega os lances e reatualiza os leilões com os lances
        LanceData lanceData = new LanceData();
        List<Lance> lances = lanceData.carregarLances(clientes, leiloes);
        leiloes = leilaoData.carregarLeiloes(clientes, lances);  // Atualiza os leilões com os lances
        leilaoController.setLeiloes(leiloes);
        leilaoController.verificarStatusLeiloes(leiloes); // Verifica novamente o status após os lances


        for (Cliente cliente : clientes) {
            notificacaoController.verificarEEnviarEmailInatividade(cliente.getEmail(), cliente.getNome(), cliente.getUltimoLogin());
        }

        NotificacaoController.verificarClientesSemSaldoEEnviarEmail();


        // Exibe o menu principal
        MenuPrincipalView menuPrincipal = new MenuPrincipalView(clienteController, leilaoController);
        menuPrincipal.exibirMenu();
    }
}
