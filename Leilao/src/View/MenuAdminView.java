package View;

import Controller.ClienteController;
import Controller.LeilaoController;
import Data.ClienteData;
import Model.*;
import Data.LeilaoData;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class MenuAdminView {
    private final LeilaoController leilaoController;
    private final ClienteController clienteController;
    private final Scanner scanner;

    // Construtor
    public MenuAdminView(LeilaoController leilaoController, ClienteController clienteController) {
        this.leilaoController = leilaoController;
        this.clienteController = clienteController;
        this.scanner = new Scanner(System.in);
    }

    // Método para exibir o menu do administrador
    public void exibirMenu() {
        while (true) {
            System.out.println("\n=== Menu Administrador ===");
            System.out.println("1. Criar Leilão Eletrônico");
            System.out.println("2. Criar Leilão Carta Fechada");
            System.out.println("3. Criar Leilão Venda Direta");
            System.out.println("4. Listar Leilões");
            System.out.println("5. Transformar Cliente em Admin");
            System.out.println("6. Voltar");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir a nova linha

            switch (opcao) {
                case 1:
                    criarLeilaoEletronico();
                    break;
                case 2:
                    criarLeilaoCartaFechada();
                    break;
                case 3:
                    criarLeilaoVendaDireta();
                    break;
                case 4:
                    listarLeiloes();
                    break;
                case 5:
                    transformarClienteEmAdminPorId();
                    break;
                case 6:
                    System.out.println("Voltando...");
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }


    // Método para criar um leilão eletrônico
    private void criarLeilaoEletronico() {
        System.out.println("\n=== Criar Leilão Eletrônico ===");
        System.out.print("Nome do produto: ");
        String nomeProduto = scanner.nextLine();
        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();


        LocalDate dataInicio = solicitarData("Data de início (dd-MM-yyyy): ");


        LocalDate dataFim;
        do {
            dataFim = solicitarData("Data de fim (dd-MM-yyyy): ");
            if (dataFim.isBefore(dataInicio)) {
                System.out.println("A data de fim não pode ser anterior à data de início. Tente novamente.");
            }
        } while (dataFim.isBefore(dataInicio));

        System.out.print("Valor mínimo: ");
        double valorMinimo = scanner.nextDouble();
        System.out.print("Valor máximo: ");
        double valorMaximo = scanner.nextDouble();
        System.out.print("Múltiplo de lance: ");
        double multiploLance = scanner.nextDouble();
        scanner.nextLine();

        // Cria o leilão eletrônico
        LeilaoEletronico leilao = leilaoController.criarLeilaoEletronico(
                nomeProduto, descricao, dataInicio, dataFim, valorMinimo, valorMaximo, multiploLance
        );
        leilaoController.adicionarLeilao(leilao);

        LeilaoData leilaoData = new LeilaoData();
        leilaoData.salvarLeiloes(leilaoController.listarLeiloes());
        System.out.println("Leilão eletrônico criado com sucesso!");
    }

    // Método para criar um leilão carta fechada
    private void criarLeilaoCartaFechada() {
        System.out.println("\n=== Criar Leilão Carta Fechada ===");
        System.out.print("Nome do produto: ");
        String nomeProduto = scanner.nextLine();
        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();


        LocalDate dataInicio = solicitarData("Data de início (dd-MM-yyyy): ");


        LocalDate dataFim;
        do {
            dataFim = solicitarData("Data de fim (dd-MM-yyyy): ");
            if (dataFim.isBefore(dataInicio)) {
                System.out.println("A data de fim não pode ser anterior à data de início. Tente novamente.");
            }
        } while (dataFim.isBefore(dataInicio));

        System.out.print("Valor mínimo: ");
        double valorMinimo = scanner.nextDouble();
        scanner.nextLine();

        // Cria o leilão carta fechada
        LeilaoCartaFechada leilao = leilaoController.criarLeilaoCartaFechada(
                nomeProduto, descricao, dataInicio, dataFim, valorMinimo
        );
        leilaoController.adicionarLeilao(leilao);

        LeilaoData leilaoData = new LeilaoData();
        leilaoData.salvarLeiloes(leilaoController.listarLeiloes());
        System.out.println("Leilão carta fechada criado com sucesso!");
    }

    // Método para criar um leilão venda direta
    private void criarLeilaoVendaDireta() {
        System.out.println("\n=== Criar Leilão Venda Direta ===");
        System.out.print("Nome do produto: ");
        String nomeProduto = scanner.nextLine();
        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();


        LocalDate dataInicio = solicitarData("Data de início (dd-MM-yyyy): ");


        LocalDate dataFim;
        do {
            dataFim = solicitarData("Data de fim (dd-MM-yyyy): ");
            if (dataFim.isBefore(dataInicio)) {
                System.out.println("A data de fim não pode ser anterior à data de início. Tente novamente.");
            }
        } while (dataFim.isBefore(dataInicio));

        System.out.print("Valor mínimo: ");
        double valorMinimo = scanner.nextDouble();
        scanner.nextLine();

        // Cria o leilão venda direta
        LeilaoVendaDireta leilao = leilaoController.criarLeilaoVendaDireta(
                nomeProduto, descricao, dataInicio, dataFim, valorMinimo
        );
        leilaoController.adicionarLeilao(leilao);

        LeilaoData leilaoData = new LeilaoData();
        leilaoData.salvarLeiloes(leilaoController.listarLeiloes());
        System.out.println("Leilão venda direta criado com sucesso!");
    }

    // Método para solicitar uma data com validação
    private LocalDate solicitarData(String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem);
                String dataStr = scanner.nextLine();
                return LocalDate.parse(dataStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            } catch (DateTimeParseException e) {
                System.out.println("Data inválida. Tente novamente.");
            }
        }
    }

    // Método para listar todos os leilões
    private void listarLeiloes() {
        System.out.println("\n=== Lista de Todos os Leilões ===");
        List<Leilao> leiloes = leilaoController.listarLeiloes();

        if (leiloes.isEmpty()) {
            System.out.println("Nenhum leilão cadastrado.");
        } else {
            // Exibe os detalhes de cada leilão
            for (Leilao leilao : leiloes) {
                System.out.println("\nTipo de Leilão: " + leilao.getTipoLeilao());
                System.out.println("Nome do Produto: " + leilao.getNomeProduto());
                System.out.println("Descrição: " + leilao.getDescricao());
                System.out.println("Data de Início: " + leilao.getDataInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                System.out.println("Data de Fim: " + leilao.getDataFim().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                System.out.println("Valor Mínimo: " + leilao.getValorMinimo());

                if (leilao instanceof LeilaoEletronico) {
                    LeilaoEletronico leilaoEletronico = (LeilaoEletronico) leilao;
                    System.out.println("Valor Máximo: " + leilaoEletronico.getValorMaximo());
                    System.out.println("Múltiplo de Lance: " + leilaoEletronico.getMultiploLance());
                }
            }
        }
    }

    private void transformarClienteEmAdminPorId() {
        System.out.println("\n=== Transformar Cliente em Administrador ===");
        List<Cliente> clientes = clienteController.listarClientes();
        if (clientes.isEmpty() ) {
            System.out.println("Não há clientes cadastrados.");
        } else {
            System.out.println("\n=== Lista de Clientes ===");
            for (Cliente cliente : clientes)
                if (!cliente.isAdmin()){
                    System.out.println("ID: " + cliente.getId() + " | E-mail: " + cliente.getEmail());
                }
        }

        System.out.print("\nInsira o ID do cliente que deseja tornar administrador (Insira 0 para voltar): ");
        int id = scanner.nextInt();
        scanner.nextLine();

        if(id == 0){
            System.out.println("A voltar para o menu...");
            return;
        }

        if (clienteController.tornarClienteAdminPorId(id)) {
            System.out.println("Cliente transformado em administrador com sucesso!");
        } else {
            System.out.println("Cliente não encontrado.");
        }
        ClienteData clienteData = new ClienteData();
        clienteData.salvarClientes(clienteController.listarClientes());
    }


}


