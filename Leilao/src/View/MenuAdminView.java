package View;

import Controller.ClienteController;
import Controller.EstatisticaController;
import Controller.LeilaoController;
import Controller.NotificacaoController;
import Data.ClienteData;
import Data.PedidoSaldoData;
import Model.*;
import Data.LeilaoData;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class MenuAdminView {
    private final LeilaoController leilaoController;
    private final ClienteController clienteController;
    private final EstatisticasView estatisticasView;
    private final Scanner scanner;

    public MenuAdminView(LeilaoController leilaoController, ClienteController clienteController, EstatisticaController estatisticaController) {
        this.leilaoController = leilaoController;
        this.clienteController = clienteController;
        this.estatisticasView = new EstatisticasView(estatisticaController,leilaoController,this);
        this.scanner = new Scanner(System.in);
    }


    // Método para exibir o menu do administrador
    public void exibirMenu() {
        while (true) {
            System.out.println("\n=== Menu Administrador ===");
            System.out.println("1. Criar Leilão Eletrônico");
            System.out.println("2. Criar Leilão Carta Fechada");
            System.out.println("3. Criar Leilão Venda Direta");
            System.out.println("4. Remover Leilão");
            System.out.println("5. Listar Leilões");
            System.out.println("6. Gerir pedidos de saldo");
            System.out.println("7. Gerir pedidos de clientes pendentes");
            System.out.println("8. Ver histórico de lances");
            System.out.println("9. Menu de Estatisticas");
            System.out.println("0. Voltar");
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
                    removerLeilao();
                    break;
                case 5:
                    listarLeiloes();
                    break;
                case 6:
                    gerirPedidosSaldo();
                    break;
                case 7:
                    gerirClientesPendentes();
                    break;
                case 8:
                    verHistoricoDeLances();
                    break;
                case 9:
                    estatisticasView.exibirMenu();
                    break;
                case 0:
                    System.out.println("A voltar...");
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


        LocalDateTime dataInicio;
        LocalDateTime hoje;
        do{
            dataInicio = solicitarDataInicio("Data de início (dd-MM-yyyy): ");
            hoje = LocalDateTime.now();
            if(dataInicio.isBefore(hoje)){
                System.out.println("A data de início do leilão tem que ser hoje ou depois de hoje!");
            }
        } while (dataInicio.isBefore(hoje));


        LocalDateTime dataFim;
        do {
            dataFim = solicitarDataFim("Data de fim (dd-MM-yyyy): ");
            if (dataFim.isBefore(dataInicio)) {
                System.out.println("A data de fim não pode ser anterior à data de início. Tente novamente.");
            }
        } while (dataFim.isBefore(dataInicio));

        System.out.print("Valor mínimo: ");
        double valorMinimo = scanner.nextDouble();
        System.out.print("Múltiplo de lance: ");
        double multiploLance = scanner.nextDouble();
        scanner.nextLine();


        boolean isAtivo = false;
        boolean isFechado = false;
        // Cria o leilão eletrônico
        LeilaoEletronico leilao = leilaoController.criarLeilaoEletronico(
                nomeProduto, descricao, dataInicio, dataFim, valorMinimo, multiploLance, isAtivo, isFechado
        );
        boolean adicionado = leilaoController.adicionarLeilao(leilao);
        if (!adicionado) {
            System.err.println("Erro: Não foi possível adicionar o leilão!");
            return;
        }

        List<Leilao> leiloes = LeilaoController.listarLeiloes();
        leilaoController.verificarStatusLeiloes(leiloes, clienteController);
        LeilaoData.salvarLeiloes(leiloes);
        System.out.println("Leilão eletrônico criado com sucesso!");
    }

    // Método para criar um leilão carta fechada
    private void criarLeilaoCartaFechada() {
        System.out.println("\n=== Criar Leilão Carta Fechada ===");
        System.out.print("Nome do produto: ");
        String nomeProduto = scanner.nextLine();
        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();

        LocalDateTime dataInicio;
        LocalDateTime hoje;
        do{
            dataInicio = solicitarDataInicio("Data de início (dd-MM-yyyy): ");
            hoje = LocalDateTime.now();
            if(dataInicio.isBefore(hoje)){
                System.out.println("A data de início do leilão tem que ser hoje ou depois de hoje!");
            }
        } while (dataInicio.isBefore(hoje));



        LocalDateTime dataFim;
        do {
            dataFim = solicitarDataFim("Data de fim (dd-MM-yyyy): ");
            if (dataFim.isBefore(dataInicio)) {
                System.out.println("A data de fim não pode ser anterior à data de início. Tente novamente.");
            }
        } while (dataFim.isBefore(dataInicio));

        System.out.print("Valor mínimo: ");
        double valorMinimo = scanner.nextDouble();
        scanner.nextLine();

        boolean isAtivo = false;
        boolean isFechado = false;
        // Cria o leilão carta fechada
        LeilaoCartaFechada leilao = leilaoController.criarLeilaoCartaFechada(
                nomeProduto, descricao, dataInicio, dataFim, valorMinimo, isAtivo, isFechado
        );
        boolean adicionado = leilaoController.adicionarLeilao(leilao);
        if (!adicionado) {
            System.err.println("Erro: Não foi possível adicionar o leilão!");
            return;
        }

        List<Leilao> leiloes = LeilaoController.listarLeiloes();
        leilaoController.verificarStatusLeiloes(leiloes, clienteController);
        LeilaoData.salvarLeiloes(leiloes);
        System.out.println("Leilão carta fechada criado com sucesso!");
    }

    // Método para criar um leilão venda direta
    private void criarLeilaoVendaDireta() {
        System.out.println("\n=== Criar Leilão Venda Direta ===");
        System.out.print("Nome do produto: ");
        String nomeProduto = scanner.nextLine();
        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();


        LocalDateTime dataInicio;
        LocalDateTime hoje;
        do{
            dataInicio = solicitarDataInicio("Data de início (dd-MM-yyyy): ");
            hoje = LocalDateTime.now();
            if(dataInicio.isBefore(hoje)){
                System.out.println("A data de início do leilão tem que ser hoje ou depois de hoje!");
            }
        } while (dataInicio.isBefore(hoje));

        LocalDateTime dataFim;
        do {
            dataFim = solicitarDataFim("Data de fim (dd-MM-yyyy): ");
            if (dataFim.isBefore(dataInicio)) {
                System.out.println("A data de fim não pode ser anterior à data de início. Tente novamente.");
            }
        } while (dataFim.isBefore(dataInicio));

        System.out.print("Valor: ");
        double valorMinimo = scanner.nextDouble();
        scanner.nextLine();

        boolean isAtivo = false;
        boolean isFechado = false;

        // Cria o leilão venda direta
        LeilaoVendaDireta leilao = leilaoController.criarLeilaoVendaDireta(
                nomeProduto, descricao, dataInicio, dataFim, valorMinimo, isAtivo, isFechado
        );
        boolean adicionado = leilaoController.adicionarLeilao(leilao);
        if (!adicionado) {
            System.err.println("Erro: Não foi possível adicionar o leilão!");
            return;
        }

        List<Leilao> leiloes = LeilaoController.listarLeiloes();
        leilaoController.verificarStatusLeiloes(leiloes, clienteController);
        LeilaoData.salvarLeiloes(leiloes);
        System.out.println("Leilão venda direta criado com sucesso!");
    }

    private void removerLeilao(){
        System.out.println(" === Remover Leilão ===");
        if(!LeilaoController.listarLeiloes().isEmpty()) {
            listarLeiloes();
        }
        else{
            System.out.println("Não existe leilões no momento.");
            return;
        }

        System.out.print("\nInsira o ID do leilão que deseja remover: (Insira 0 para voltar): ");
        int id = scanner.nextInt();
        scanner.nextLine();

        if(id == 0){
            System.out.println("A voltar para o menu...");
            return;
        }

        if(leilaoController.removerLeilao(id)){
            System.out.println("Leilão removido com sucesso!");
        }
        else{
            System.out.println("ID inválido!");
        }

        LeilaoData.salvarLeiloes(LeilaoController.listarLeiloes());


    }

    private LocalDateTime solicitarDataInicio(String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem);
                String dataStr = scanner.nextLine();
                LocalDate data = LocalDate.parse(dataStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                return LocalDateTime.of(data, LocalTime.now());
            } catch (DateTimeParseException e) {
                System.out.println("Data inválida. Use o formato dd-MM-yyyy.");
            }
        }
    }

    private LocalDateTime solicitarDataFim(String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem);
                String dataStr = scanner.nextLine();
                LocalDate data = LocalDate.parse(dataStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                return LocalDateTime.of(data, LocalTime.of(23, 59, 59));
            } catch (DateTimeParseException e) {
                System.out.println("Data inválida. Use o formato dd-MM-yyyy.");
            }
        }
    }

    // Método para listar todos os leilões
    private void listarLeiloes() {
        System.out.println("\n=== Lista de Todos os Leilões ===");
        List<Leilao> leiloes = LeilaoController.listarLeiloes();

        if (leiloes.isEmpty()) {
            System.out.println("Nenhum leilão cadastrado.");
        } else {
            // Exibe os detalhes de cada leilão
            for (Leilao leilao : leiloes) {
                System.out.println("\nID: " + leilao.getId());
                System.out.println("Tipo de Leilão: " + leilao.getTipoLeilao());
                System.out.println("Nome do Produto: " + leilao.getNomeProduto());
                System.out.println("Descrição: " + leilao.getDescricao());
                System.out.println("Data de Início: " + leilao.getDataInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                System.out.println("Data de Fim: " + leilao.getDataFim().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                if(leilao instanceof  LeilaoVendaDireta){
                    System.out.println("Valor: " + leilao.getValorMinimo());
                    if(!leilao.isFechado()){
                        System.out.println("STATUS: " + (leilao.isAtivo() ? "Ativo" : "Inativo"));
                    }else{
                        System.out.println("STATUS: Fechado " );
                    }
                    if (leilao.getVencedor() != null) {
                        System.out.println("Vencedor: " + leilao.getVencedor().getEmail());
                    } else {
                        System.out.println("Ainda não foi anunciado o vencedor. ");
                    }
                }else if(leilao instanceof LeilaoEletronico leilaoEletronico){
                    System.out.println("Valor Mínimo: " + leilao.getValorMinimo());
                    System.out.println("Múltiplo de Lance: " + leilaoEletronico.getMultiploLance());
                    if(!leilao.isFechado()){
                        System.out.println("STATUS: " + (leilao.isAtivo() ? "Ativo" : "Inativo"));
                    }else{
                        System.out.println("STATUS: Fechado " );
                    }
                    System.out.println("Clientes Inscritos:");

                    if (leilao.getClientesInscritos().isEmpty()) {
                        System.out.println("  Nenhum cliente inscrito");
                    } else {
                        leilao.getClientesInscritos().forEach(cliente ->
                                System.out.println("  - " + cliente.getEmail()));
                    }
                    if (leilao.getVencedor() != null) {
                        System.out.println("Vencedor: " + leilao.getVencedor().getEmail());
                    } else {
                        System.out.println("Ainda não foi anunciado o vencedor. ");
                    }
                }else{
                    System.out.println("Valor Mínimo: " + leilao.getValorMinimo());
                    if(!leilao.isFechado()){
                        System.out.println("STATUS: " + (leilao.isAtivo() ? "Ativo" : "Inativo"));
                    }else{
                        System.out.println("STATUS: Fechado " );
                    }
                    System.out.println("Clientes Inscritos:");

                    if (leilao.getClientesInscritos().isEmpty()) {
                        System.out.println("  Nenhum cliente inscrito");
                    } else {
                        leilao.getClientesInscritos().forEach(cliente ->
                                System.out.println("  - " + cliente.getEmail()));
                    }
                    if (leilao.getVencedor() != null) {
                        System.out.println("Vencedor: " + leilao.getVencedor().getEmail());
                    } else {
                        System.out.println("Ainda não foi anunciado o vencedor. ");
                    }
                }



            }
        }

    }


    private void gerirPedidosSaldo() {
        System.out.println("\n=== Gerir Pedidos de Saldo ===");

        List<PedidoSaldo> todosPedidos = PedidoSaldoData.carregarPedidos(clienteController.getClientes());

        List<PedidoSaldo> pedidosPendentes = todosPedidos.stream()
                .filter(p -> p.isAprovado() == null)
                .toList();

        if (pedidosPendentes.isEmpty()) {
            System.out.println("Não há pedidos de saldo pendentes.");
            return;
        }

        System.out.println("\n══════════════════════════════════════════════════════════════════════");
        System.out.printf("| %-4s | %-20s | %-10s | %-12s |\n",
                "ID", "Cliente", "Quantia", "Data Pedido");
        System.out.println("══════════════════════════════════════════════════════════════════════");

        for (int i = 0; i < pedidosPendentes.size(); i++) {
            PedidoSaldo pedido = pedidosPendentes.get(i);
            System.out.printf("| %-4d | %-20s | %-10.2f | %-12s |\n",
                    i + 1,
                    pedido.getCliente().getNome(),
                    pedido.getQuantia(),
                    pedido.getDataPedido().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
        System.out.println("══════════════════════════════════════════════════════════════════════");

        System.out.print("\nDigite o ID do pedido para gereir (ou 0 para voltar): ");
        int idPedido = scanner.nextInt();
        scanner.nextLine();

        if (idPedido == 0) {
            return;
        }

        if (idPedido < 1 || idPedido > pedidosPendentes.size()) {
            System.out.println("ID inválido!");
            return;
        }

        PedidoSaldo pedidoSelecionado = pedidosPendentes.get(idPedido - 1);

        System.out.println("\nPedido selecionado:");
        System.out.println("Cliente: " + pedidoSelecionado.getCliente().getNome());
        System.out.println("Quantia: " + pedidoSelecionado.getQuantia());
        System.out.println("Data: " + pedidoSelecionado.getDataPedido().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        System.out.println("\n1. Aprovar pedido");
        System.out.println("2. Rejeitar pedido");
        System.out.println("3. Voltar");
        System.out.print("Escolha: ");
        int opcao = scanner.nextInt();
        scanner.nextLine();

        switch (opcao) {
            case 1:
                pedidoSelecionado.setAprovado(true);
                clienteController.aprovarPedidoSaldo(pedidoSelecionado);
                PedidoSaldoData.salvarPedidos(todosPedidos);
                ClienteData.salvarClientes(ClienteController.listarClientes());

                System.out.println("Pedido aprovado com sucesso!");
                break;

            case 2:
                pedidoSelecionado.setAprovado(false);
                PedidoSaldoData.salvarPedidos(todosPedidos);

                System.out.println("Pedido rejeitado com sucesso!");
                break;

            case 3:
                System.out.println("Operação cancelada.");
                break;

            default:
                System.out.println("Opção inválida!");
        }
    }


    private void gerirClientesPendentes() {
        List<Cliente> pendentes = clienteController.listarClientesPendentes();
        if (pendentes.isEmpty()) {
            System.out.println("Nenhum cliente pendente.");
            return;
        }

        System.out.println("\n=== Clientes Pendentes ===");
        System.out.println("\n════════════════════════════════════════════════════════════════════════════════════════");
        System.out.printf("| %-4s | %-20s | %-25s | %-12s |\n",
                "ID", "Nome", "Email", "Nascimento");
        System.out.println("════════════════════════════════════════════════════════════════════════════════════════");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (int i = 0; i < pendentes.size(); i++) {
            Cliente c = pendentes.get(i);
            System.out.printf("| %-4d | %-20s | %-25s | %-12s |\n",
                    i + 1,
                    c.getNome(),
                    c.getEmail(),
                    c.getDataNascimento().format(formatter));
        }

        System.out.println("════════════════════════════════════════════════════════════════════════════════════════");


        System.out.print("Selecione o cliente (0 para cancelar): ");
        int escolha = scanner.nextInt();
        if (escolha > 0 && escolha <= pendentes.size()) {
            Cliente selecionado = pendentes.get(escolha-1);
            System.out.print("Aprovar (A) ou Rejeitar (R)? ");
            String decisao = scanner.next().toUpperCase();

            if (decisao.equals("A")) {
                clienteController.aprovarCliente(selecionado, true);
                System.out.println("Cliente aprovado com sucesso!");
                NotificacaoController notificacaoController = new NotificacaoController();
                notificacaoController.enviarEmail(selecionado.getEmail(), selecionado.getNome());
                ClienteData.salvarClientes(ClienteController.listarClientes());

            } else if (decisao.equals("R")) {
                clienteController.aprovarCliente(selecionado, false);
                System.out.println("Cliente rejeitado.");
                ClienteData.salvarClientes(ClienteController.listarClientes());
            }
        }
    }

    private void verHistoricoDeLances() {
        List<Leilao> leiloes = LeilaoController.listarLeiloes();

        if (leiloes.isEmpty()) {
            System.out.println("Nenhum leilão disponível.");
            return;
        }

        System.out.println("\n=== Leilões Disponíveis ===");
        for (Leilao l : leiloes) {
            String estado = l.isFechado() ? "Fechado" : "Aberto";
            System.out.printf("ID %d - %s [%s]\n", l.getId(), l.getNomeProduto(), estado);
        }

        System.out.print("Digite o ID do leilão desejado (0 para cancelar): ");
        int idEscolhido = scanner.nextInt();
        scanner.nextLine();

        if (idEscolhido == 0) {
            System.out.println("Operação cancelada.");
            return;
        }

        Leilao selecionado = null;
        for (Leilao l : leiloes) {
            if (l.getId() == idEscolhido) {
                selecionado = l;
                break;
            }
        }

        if (selecionado == null) {
            System.out.println("Leilão não encontrado");
            return;
        }

        List<Lance> lances = leilaoController.obterLancesPorLeilao(selecionado);

        if (lances.isEmpty()) {
            System.out.println("Nenhum lance registrado neste leilão.");
            return;
        }

        System.out.println("\n═══════════════════════════════════════════════════════════════════════════════════");
        System.out.printf("| %-3s | %-20s | %-25s | %-10s | %-19s |\n",
                "IDº", "Cliente", "Email", "Valor", "Data do Lance");
        System.out.println("═══════════════════════════════════════════════════════════════════════════════════");

        for (int i = 0; i < lances.size(); i++) {
            Lance l = lances.get(i);
            System.out.printf("| %-3d | %-20s | %-25s | %-10.2f | %-19s |\n",
                    i + 1,
                    l.getCliente().getNome(),
                    l.getCliente().getEmail(),
                    l.getValor(),
                    l.getDataHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
            );
        }

        System.out.println("═══════════════════════════════════════════════════════════════════════════════════");
    }



}





