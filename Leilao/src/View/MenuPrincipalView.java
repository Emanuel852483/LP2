package View;

import Controller.*;
import Data.AvaliacaoLeilaoData;
import Data.ClienteData;
import Data.NotaData;
import Model.Cliente;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class MenuPrincipalView {
    private final ClienteController clienteController;
    private final LeilaoController leilaoController;
    private final AgenteController agenteController;
    private final EstatisticaController estatisticaController;
    private final Scanner scanner;

    public MenuPrincipalView() {
        this.clienteController = new ClienteController();
        this.leilaoController = new LeilaoController(clienteController);
        this.agenteController = new AgenteController(leilaoController);
        leilaoController.setAgenteController(agenteController);

        this.leilaoController.verificarStatusLeiloes();

        this.estatisticaController = new EstatisticaController(clienteController, leilaoController);

        this.scanner = new Scanner(System.in);
    }


    // Método para exibir o menu principal
    public void exibirMenu() {
        while (true) {
            clienteController.verificarInatividadeClientes();

            System.out.println("\n=== Menu Principal ===");
            System.out.println("1. Login");
            System.out.println("2. Registar Novo Cliente");
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
                    NotificacaoController.gerarRelatoriosCSVSeparados(LeilaoController.listarLeiloes(), ClienteController.listarClientes());
                    NotificacaoController.enviarRelatoriosCSVparaGestor();
                    System.out.println("Saindo...");
                    System.exit(0);
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
            if ("PENDENTE".equals(cliente.getStatus())) {
                System.out.println("Aguarde pela a aprovação do administrador.");
                return;
            } else if ("REJEITADO".equals(cliente.getStatus())) {
                System.out.println("Cadastro rejeitado.");
                return;
            } else {
                System.out.println("Login bem-sucedido! Bem-vindo, " + cliente.getNome() + ".");
                cliente.setUltimoLogin(LocalDateTime.now());
                ClienteData.salvarClientes(ClienteController.listarClientes());
            }

            if (cliente.isAdmin()) {
                MenuAdminView menuAdmin = new MenuAdminView(leilaoController, clienteController, estatisticaController);
                menuAdmin.exibirMenu();
            } else {
                MenuClienteView menuCliente = new MenuClienteView(cliente, clienteController, leilaoController, new AgenteView(agenteController));
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

                LocalDate hoje = LocalDate.now();
                Period periodo = Period.between(dataNascimento, hoje);
                if(periodo.getYears() < 18){
                    System.out.println("O usuário tem que ter mais de 18 anos!");

                }
                else{
                    dataValida = true;
                }


            }
            catch (DateTimeParseException e) {
                System.out.println("Data inválida. Tente novamente:");
            }
        }

        String email = "";
        boolean emailValido = false;

        while (!emailValido) {
            System.out.print("E-mail: ");
            email = scanner.nextLine().trim();


            if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                System.out.println("Formato de e-mail inválido. Use o formato exemplo@dominio.com");
                continue;
            }


            if (clienteController.existeEmail(email)) {
                System.out.println("Este e-mail já se encontra cadastrado. Por favor, use outro.");
            } else {
                emailValido = true;
            }
        }

        System.out.print("Password: ");
        String password = scanner.nextLine().trim();


        int lancesDisponiveis = 0;
        boolean isAdmin = false;
        double saldo = 0;
        String status = "PENDENTE";
        LocalDateTime ultimoLogin = LocalDateTime.now();
        Cliente novoCliente = clienteController.criarCliente(nome, morada, dataNascimento, email, password, lancesDisponiveis,isAdmin,saldo, ultimoLogin, status);
        boolean sucesso = clienteController.adicionarCliente(novoCliente);
        if (!sucesso) {
            System.err.println("Erro: Não foi possível adicionar o cliente!");
            return;
        }
        System.out.println("Cliente registrado com sucesso!");
        System.out.println("Cliente pendente, por favor aguarde até a aprovação do administrador!");



        ClienteData.salvarClientes(ClienteController.listarClientes());

    }


}
