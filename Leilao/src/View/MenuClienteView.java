package View;

import Controller.LeilaoController;
import Controller.ClienteController;
import Data.ClienteData;
import Data.LanceData;
import Data.LeilaoData;
import Model.*;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;



public class MenuClienteView {
    private final Cliente cliente;
    private final ClienteController clienteController;
    private final LeilaoController leilaoController;
    private final Scanner scanner;

    // Construtor
    public MenuClienteView(Cliente cliente, ClienteController clienteController, LeilaoController leilaoController) {
        this.cliente = cliente;
        this.clienteController = clienteController;
        this.leilaoController = leilaoController;
        this.scanner = new Scanner(System.in);
    }

    // Método para exibir o menu do cliente
    public void exibirMenu() {
        while (true) {
            System.out.println("\n=== Menu Cliente ===");
            System.out.println("1. Alterar meus dados");
            System.out.println("2. Ver leilões em que estou inscrito");
            System.out.println("3. Ver leilões a terminar");
            System.out.println("4. Ver leilões ativos");
            System.out.println("5. Ver todos os leilões");
            System.out.println("6. Comprar lances");
            System.out.println("7. Verificar Lances Disponíveis");
            System.out.println("8. Realizar lance em leilão");
            System.out.println("9. Inscrever-se em um leilão");
            System.out.println("10. Sair");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir a nova linha

            switch (opcao) {
                case 1:
                    alterarDados();
                    break;
                case 2:
                    verLeiloesInscritos();
                    break;
                case 3:
                    verLeiloesATerminar();
                    break;
                case 4:
                    verLeiloesAtivos();
                    break;
                case 5:
                    verTodosLeiloes();
                    break;
                case 6:
                    comprarLances();
                    break;
                case 7:
                    verificarLancesDisponiveis();
                    break;
                case 8:
                    realizarLance();
                    break;
                case 9:
                    inscreverEmLeilao();
                    break;
                case 10:
                    System.out.println("Saindo...");
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    // Método para alterar os dados do cliente
    private void alterarDados() {
        System.out.println("\n=== Alterar Meus Dados ===");

        // Nome
        System.out.print("Novo nome (deixe em branco para manter o atual): ");
        String novoNome = scanner.nextLine();
        if (!novoNome.isBlank()) {
            cliente.setNome(novoNome);
        }

        // Morada
        System.out.print("Nova morada (deixe em branco para manter a atual): ");
        String novaMorada = scanner.nextLine();
        if (!novaMorada.isBlank()) {
            cliente.setMorada(novaMorada);
        }

        // Data de Nascimento
        LocalDate novaData = null;
        boolean dataValida = false;
        while (!dataValida) {
            System.out.print("Nova data de nascimento (dd-MM-yyyy) (deixe em branco para manter a atual): ");
            String novaDataStr = scanner.nextLine();
            if (novaDataStr.isBlank()) {
                dataValida = true; // Sai do loop se o campo for deixado em branco
            } else {
                try {
                    novaData = LocalDate.parse(novaDataStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));

                    LocalDate hoje = LocalDate.now();
                    Period periodo = Period.between(novaData, hoje);
                    if(periodo.getYears() < 18){
                        System.out.println("O usuário tem que ter mais de 18 anos!");
                    }
                    else{
                        dataValida = true;
                    }


                } catch (DateTimeParseException e) {
                    System.out.println("Data inválida. Tente novamente:");
                }
            }
        }
        if (novaData != null) {
            cliente.setDataNascimento(novaData);
        }



        System.out.print("Nova password (deixe em branco para manter a atual): ");
        String novaPassword = scanner.nextLine().trim();
        if (!novaPassword.isBlank()) {
            cliente.setPassword(novaPassword);
        }

        System.out.println("Dados atualizados com sucesso!");

        ClienteData clienteData = new ClienteData();
        clienteData.salvarClientes(clienteController.listarClientes());
    }

    // Método para ver leilões em que o cliente está inscrito
    private void verLeiloesInscritos() {
        System.out.println("\n=== Leilões em que está inscrito ===");
        List<Leilao> leiloesInscritos = leilaoController.listarLeiloesPorCliente(cliente);
        if (leiloesInscritos.isEmpty()) {
            System.out.println("Você não está inscrito em nenhum leilão.");
        } else {
            System.out.println("\n═════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════");
            System.out.printf(" %-4s | %-20s | %-20s| %-15s | %-15s | %-15s | | %-15s  \n", "ID", "Produto", "Tipo de Leilão", "Data Início", "Data Fim" , "Valor Mínimo", "Status");
            System.out.println("════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════");

            for (Leilao leilao : leiloesInscritos) {
                String status = leilao.isFechado() ? "Fechado" :
                        (leilao.isAtivo() ? "Ativo" : "Inativo");
                System.out.printf(" %-4d | %-20s | %-20s| %-15s | %-15s | %-15s | %-15s\n",
                        leilao.getId(),
                        leilao.getNomeProduto(),
                        leilao.getTipoLeilao(),
                        leilao.getDataInicio(),
                        leilao.getDataFim(),
                        leilao.getValorMinimo(),
                        status);
            }
            System.out.println("═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════");
        }
    }

    // Método para ver leilões a terminar
    private void verLeiloesATerminar() {
        System.out.println("\n=== Leilões a terminar ===");
        List<Leilao> leiloesATerminar = leilaoController.listarLeiloesATerminar();
        if (leiloesATerminar.isEmpty()) {
            System.out.println("Não há leilões a terminar.");
        } else {
            System.out.println("\n═════════════════════════════════════════════════════════════════════════════");
            System.out.printf(" %-4s | %-20s | %-20s | %-15s \n", "ID", "Produto", "Tipo Leilão", "Data de Fim");
            System.out.println("══════════════════════════════════════════════════════════════════════════════");

            for (Leilao leilao : leiloesATerminar) {
                System.out.printf(" %-4d | %-20s | | %-20s | %-15s \n",
                        leilao.getId(),
                        leilao.getNomeProduto(),
                        leilao.getTipoLeilao(),
                        leilao.getDataFim() );

            }
            System.out.println("════════════════════════════════════════════════════════════════════════════");
        }

    }

    // Método para ver leilões ativos
    private void verLeiloesAtivos() {
        System.out.println("\n=== Leilões ativos ===");
        List<Leilao> leiloesAtivos = leilaoController.listarLeiloesAtivos();
        if (leiloesAtivos.isEmpty()) {
            System.out.println("Não há leilões ativos no momento.");
        } else {
            System.out.println("\n════════════════════════════════════════════════════════════════════════════════════════════════════");
            System.out.printf(" %-4s | %-20s | %-20s | %-15s | %-15s \n", "ID", "Produto", "Tipo de Leilão", "Data Início", "Data Fim");
            System.out.println("══════════════════════════════════════════════════════════════════════════════════════════════════════");
            for (Leilao leilao : leiloesAtivos) {
                System.out.printf(" %-4d | %-20s | %-20s | %-15s | %-15s \n",
                        leilao.getId(),
                        leilao.getNomeProduto(),
                        leilao.getTipoLeilao(),
                        leilao.getDataInicio(),
                        leilao.getDataFim() );
            }
            System.out.println("══════════════════════════════════════════════════════════════════════════════════════════════════════");
        }
    }

    // Método para ver todos os leilões
    private void verTodosLeiloes() {
        System.out.println("\n=== Todos os leilões ===");
        List<Leilao> todosLeiloes = leilaoController.listarLeiloes();

        if (todosLeiloes.isEmpty()) {
            System.out.println("Não há leilões cadastrados.");
        } else {
            System.out.println("═════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════");
            System.out.printf("| %-4s | %-15s | %-20s | %-15s | %-15s | | %-15s \n", "ID", "Produto", "Tipo de Leilão", "Data de Início", "Data Fim", "Status");
            System.out.println("═════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════");

            for (Leilao leilao : todosLeiloes) {
                String status = leilao.isFechado() ? "Fechado" :
                        (leilao.isAtivo() ? "Ativo" : "Inativo");
                System.out.printf("| %-4d | %-15s | %-20s | %-15s | %-15s | %-15s\n",
                        leilao.getId(),
                        leilao.getNomeProduto(),
                        leilao.getTipoLeilao(),
                        leilao.getDataInicio(),
                        leilao.getDataFim(),
                        status);
            }

            System.out.println("════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════");

        }
    }

    // Método para comprar lances
    private void comprarLances() {
        System.out.println("\n=== Comprar lances ===");
        System.out.print("Quantidade de lances a comprar: ");
        int quantidade = scanner.nextInt();
        scanner.nextLine(); // Consumir a nova linha

        // Chama o método do Controller para comprar lances
        boolean sucesso = clienteController.comprarLances(cliente, quantidade);

        // Exibe o resultado para o usuário
        if (sucesso) {
            System.out.println(quantidade + " lances comprados com sucesso!");
            System.out.println("Total de lances disponíveis: " + cliente.getLancesDisponiveis());
            ClienteData clienteData = new ClienteData();
            clienteData.salvarClientes(clienteController.listarClientes());

        } else {
            System.out.println("Quantidade inválida. Deve ser maior que zero.");
        }
    }

    private void inscreverEmLeilao() {
        System.out.println("\n=== Inscrever-se em um leilão ===");

        // Listar apenas leilões que não são de venda direta
        List<Leilao> leiloesParaInscricao = leilaoController.listarLeiloes().stream()
                .filter(l -> !(l instanceof LeilaoVendaDireta) && !l.isFechado())
                .toList();

        if (leiloesParaInscricao.isEmpty()) {
            System.out.println("Não há leilões disponíveis para inscrição no momento.");
            return;
        }

        System.out.println("\n═════════════════════════════════════════════════════════════════════════════════════════════");
        System.out.printf("| %-4s | %-20s | %-15s | %-12s | %-15s | | %-15s |\n",
                "ID", "Produto", "Tipo", "Valor Mínimo", "Data Início", "Data Fim");
        System.out.println("═══════════════════════════════════════════════════════════════════════════════════════════════");

        for (Leilao leilao : leiloesParaInscricao) {
            String tipo = leilao instanceof LeilaoEletronico ? "Eletrônico" : "Carta Fechada";

            System.out.printf("| %-4d | %-20s | %-15s | %-12.2f | %-15s | | %-15s |\n",
                    leilao.getId(),
                    leilao.getNomeProduto(),
                    tipo,
                    leilao.getValorMinimo(),
                    leilao.getDataInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    leilao.getDataFim().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
        System.out.println("═════════════════════════════════════════════════════════════════════════════════════════════════");

        System.out.print("\nQual o leilão que deseja se inscrever? (Insira o ID ou 0 para sair): ");
        int idLeilao = scanner.nextInt();
        scanner.nextLine();

        if (idLeilao == 0) {
            System.out.println("Operação cancelada.");
            return;
        }

        Leilao leilao = leilaoController.buscarLeilaoPorId(idLeilao);

        if (leilao != null) {
            // Verifica se é leilão de venda direta
            if (leilao instanceof LeilaoVendaDireta) {
                System.out.println("O cliente não precisa se inscrever em leilões de venda direta!");
                return;
            }

            if (leilao.isFechado()) {
                System.out.println("O cliente não pode se inscrever em leilões que já se encontram fechados!");
                return;
            }

            boolean inscrito = leilaoController.inscreverClienteEmLeilao(leilao, cliente);

            if (inscrito) {
                LeilaoData leilaoData = new LeilaoData();
                leilaoData.salvarLeiloes(leilaoController.listarLeiloes());

                System.out.println("Inscrição realizada com sucesso no leilão: " + leilao.getNomeProduto());
            } else {
                System.out.println("Você já está inscrito neste leilão.");
            }
        } else {
            System.out.println("Leilão não encontrado com o ID: " + idLeilao);
        }
    }

    private void realizarLance() {
        System.out.println("\n=== Realizar Lance ===");


        List<Leilao> leiloesInscritos = leilaoController.listarLeiloesPorCliente(cliente).stream()
                .filter(l -> !(l instanceof LeilaoVendaDireta) && !l.isFechado()) // Exclui venda direta pois não precisa inscrição
                .toList();


        List<Leilao> leiloesVendaDireta = leilaoController.listarLeiloes().stream()
                .filter(l -> l instanceof LeilaoVendaDireta && !l.isFechado() )
                .toList();


        List<Leilao> leiloesDisponiveis = new ArrayList<>();
        leiloesDisponiveis.addAll(leiloesInscritos);
        leiloesDisponiveis.addAll(leiloesVendaDireta);

        if (leiloesDisponiveis.isEmpty()) {
            System.out.println("Não há leilões disponíveis para licitação no momento.");
            return;
        }



        System.out.println("\n═══════════════════════════════════════════════════════════════════════════════════════════════════════════");
        System.out.printf("| %-4s | %-20s | %-15s | %-12s | %-15s | %-8s |\n",
                "ID", "Produto", "Tipo", "Valor Mínimo", "Data Fim", "Status");
        System.out.println("═══════════════════════════════════════════════════════════════════════════════════════════════════════════");

        for (Leilao leilao : leiloesDisponiveis) {
            String tipoleilao = leilao instanceof LeilaoVendaDireta ? "Venda Direta" :
                    leilao instanceof LeilaoEletronico ? "Eletrônico" : "Carta Fechada";

            System.out.printf("| %-4d | %-20s | %-15s | %-12.2f | %-15s | %-8s |\n",
                    leilao.getId(),
                    leilao.getNomeProduto(),
                    tipoleilao,
                    leilao.getValorMinimo(),
                    leilao.getDataFim().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    leilao.isAtivo() ? "Ativo" : "Inativo");
        }
        System.out.println("═════════════════════════════════════════════════════════════════════════════════════════════════════════");

        System.out.print("\nDigite o ID do leilão para licitar (ou 0 para cancelar): ");
        int idLeilao = scanner.nextInt();
        scanner.nextLine();

        if (idLeilao == 0) {
            System.out.println("Operação cancelada.");
            return;
        }

        Leilao leilaoSelecionado = leiloesDisponiveis.stream()
                .filter(l -> l.getId() == idLeilao)
                .findFirst()
                .orElse(null);

        if (leilaoSelecionado == null) {
            System.out.println("ID inválido ou leilão não disponível para licitação.");
            return;
        }

        if (!leilaoSelecionado.isAtivo()) {
            System.out.println("\nEste leilão não está ativo no momento.");
            System.out.println("Período do leilão: " +
                    leilaoSelecionado.getDataInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                    " a " +
                    leilaoSelecionado.getDataFim().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            return;
        }

        if(leilaoSelecionado.isFechado()){
            System.out.println("\nEste leilão já se encontra fechado!");
            return;
        }


        if (leilaoSelecionado instanceof LeilaoVendaDireta) {
            LeilaoVendaDireta lvd = (LeilaoVendaDireta) leilaoSelecionado;

            System.out.println("\n=== Leilão de Venda Direta ===");
            System.out.printf("Produto: %s | Valor: %.2f\n",
                    lvd.getNomeProduto(),
                    lvd.getValorMinimo());

            while (true) {
                System.out.print("Deseja adquirir este produto? (S para Sim, N para Não): ");
                String resposta = scanner.nextLine().trim().toUpperCase();

                if (resposta.equals("S")) {
                    System.out.println("Produto adquirido com sucesso!");
                    lvd.setAtivo(false);
                    lvd.setFechado(true);

                    LeilaoData leilaoData = new LeilaoData();
                    leilaoData.salvarLeiloes(leilaoController.listarLeiloes());
                    return;

                } else if (resposta.equals("N")) {
                    System.out.println("Operação cancelada.");
                    return;
                } else {
                    System.out.println("Opção inválida. Digite S ou N.");
                }
            }
        }

        // Lógica para outros tipos de leilão (eletrônico e carta fechada)
        if (leilaoSelecionado instanceof LeilaoEletronico) {
            if (cliente.getLancesDisponiveis() <= 0) {
                System.out.println("Usuário não tem lances disponíveis para leilões eletrônicos.");
                System.out.println("Lances disponíveis: " + cliente.getLancesDisponiveis());
                return;
            }

            LeilaoEletronico le = (LeilaoEletronico) leilaoSelecionado;
            System.out.println("\n=== Informações do Leilão Eletrônico ===");
            System.out.printf("Valor mínimo: %.2f | Múltiplo: %.2f\n",
                    le.getValorMinimo(), le.getMultiploLance());

            if (!le.getLances().isEmpty()) {
                System.out.printf("Último lance: %.2f\n", le.getLances().getLast().getValor());
            }
        }

        System.out.print("\nValor do lance: ");
        double valor = scanner.nextDouble();
        scanner.nextLine();

        boolean sucesso = leilaoController.registrarLance(leilaoSelecionado, cliente, valor);

        if (sucesso) {
            System.out.println("\nLance registrado com sucesso!");

            LeilaoData leilaoData = new LeilaoData();
            leilaoData.salvarLeiloes(leilaoController.listarLeiloes());

            LanceData lanceData = new LanceData();
            lanceData.salvarLances(leilaoController.obterTodosLances());

            if (leilaoSelecionado instanceof LeilaoEletronico) {
                System.out.println("Lances restantes: " + cliente.getLancesDisponiveis());
                ClienteData clienteData = new ClienteData();
                clienteData.salvarClientes(clienteController.listarClientes());
            }
        } else {
            System.out.println("\nFalha ao registrar lance. Motivos possíveis:");
            System.out.println("- Valor abaixo do mínimo permitido");
            System.out.println("- Não é múltiplo do valor requerido (para leilões eletrônicos)");
            System.out.println("- Você já participou deste leilão (para carta fechada)");
        }
    }


    private void verificarLancesDisponiveis() {
        System.out.println("\n=== Verificar Lances Disponíveis ===");
        int lancesDisponiveis = cliente.getLancesDisponiveis();
        System.out.println("Você tem " + lancesDisponiveis + " lances disponíveis.");
    }
}