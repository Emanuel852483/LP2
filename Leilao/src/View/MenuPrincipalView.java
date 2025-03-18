package View;

import Controller.ClienteController;
import Controller.LeilaoController;
import Data.ClienteData;
import Model.Cliente;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class MenuPrincipalView {
    private final ClienteController clienteController;
    private final LeilaoController leilaoController;
    private final Scanner scanner;

    // Construtor
    public MenuPrincipalView(ClienteController clienteController, LeilaoController leilaoController) {
        this.clienteController = clienteController;
        this.leilaoController = leilaoController;
        this.scanner = new Scanner(System.in);
    }

    // Método para exibir o menu principal
    public void exibirMenu() {
        while (true) {
            System.out.println("\n=== Menu Principal ===");
            System.out.println("1. Login");
            System.out.println("2. Registrar Novo Cliente");
            System.out.println("3. Sair");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir a nova linha

            switch (opcao) {
                case 1:
                    fazerLogin();
                    break;
                case 2:
                    registrarNovoCliente();
                    break;
                case 3:
                    System.out.println("Saindo...");
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }



    // Método para fazer login
    private void fazerLogin() {
        System.out.println("\n=== Login ===");
        System.out.print("E-mail: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        Cliente cliente = clienteController.autenticarCliente(email, password);
        if (cliente != null) {
            System.out.println("Login bem-sucedido! Bem-vindo, " + cliente.getNome() + ".");

            // Usa a instância de LeilaoController passada para o MenuPrincipalView
            if (cliente.isAdmin()) {
                MenuAdminView menuAdmin = new MenuAdminView(leilaoController, clienteController);
                menuAdmin.exibirMenu();
            } else {
                MenuClienteView menuCliente = new MenuClienteView(cliente, clienteController, leilaoController);
                menuCliente.exibirMenu();
            }
        } else {
            System.out.println("E-mail ou senha incorretos.");
        }
    }

    // Método para registrar um novo cliente
    private void registrarNovoCliente() {
        System.out.println("\n=== Registrar Novo Cliente ===");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Morada: ");
        String morada = scanner.nextLine();

        // Loop para garantir que a data seja válida
        LocalDate dataNascimento = null;
        boolean dataValida = false;
        while (!dataValida) {
            System.out.print("Data de Nascimento (dd-MM-yyyy): ");
            String dataNascimentoStr = scanner.nextLine();
            try {
                dataNascimento = LocalDate.parse(dataNascimentoStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                dataValida = true; // Sai do loop se a data for válida
            } catch (DateTimeParseException e) {
                System.out.println("Data inválida. Tente novamente:");
            }
        }

        System.out.print("E-mail: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        // Define um valor padrão para lancesDisponiveis (por exemplo, 0)
        int lancesDisponiveis = 0;

        // Cria e adiciona o novo cliente
        boolean isAdmin = false;
        Cliente novoCliente = clienteController.criarCliente(nome, morada, dataNascimento, email, password, lancesDisponiveis,isAdmin);
        clienteController.adicionarCliente(novoCliente);
        System.out.println("Cliente registrado com sucesso!");

        ClienteData clienteData = new ClienteData();
        clienteData.salvarClientes(clienteController.listarClientes());
    }


}

