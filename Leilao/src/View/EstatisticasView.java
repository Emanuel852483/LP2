package View;

import Controller.EstatisticaController;
import Controller.LeilaoController;
import Model.AvaliacaoLeilao;
import Model.Cliente;
import Model.Leilao;
import Model.Lance;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class EstatisticasView {
    private final EstatisticaController estatisticaController;
    private final LeilaoController leilaoController;
    private final MenuAdminView menuAdminView;
    private final Scanner scanner;

    public EstatisticasView(EstatisticaController estatisticaController, LeilaoController leilaoController, MenuAdminView menuAdminView) {
        this.estatisticaController = estatisticaController;
        this.leilaoController = leilaoController;
        this.menuAdminView = menuAdminView;
        this.scanner = new Scanner(System.in);
    }

    public void exibirMenu() {
        int opcao;

        while (true) {
            System.out.println("\n=== Estatísticas ===");
            System.out.println("1. Cliente que realizou mais lances (por leilão)");
            System.out.println("2. Tempo total ativo de um leilão");
            System.out.println("3. Quantidade de leilões terminados");
            System.out.println("4. Leilão com mais tempo ativo");
            System.out.println("5. Leilão com mais lances");
            System.out.println("6. Média de tempo até cada lance (minutos)");
            System.out.println("7. Leilões sem lances");
            System.out.println("8. Total de clientes registados");
            System.out.println("9. Média de idades dos clientes");
            System.out.println("10. Média de domínio de email mais utilizado");
            System.out.println("11. Média das avaliações dos leilões");
            System.out.println("0. Voltar");

            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();

            switch (opcao) {
                case 1:
                    mostrarClienteComMaisLancesPorValor();
                    break;
                case 2:
                    mostrarTempoAtivoDeLeilao();
                    break;
                case 3:
                    mostrarQuantidadeLeiloesTerminados();
                    break;
                case 4:
                    mostrarLeilaoComMaisTempoAtivo();
                    break;
                case 5:
                    mostrarLeilaoComMaisLances();
                    break;
                case 6:
                    mostrarEstatisticasTempoLances();
                    break;
                case 7:
                    mostrarLeiloesSemLances();
                    break;
                case 8:
                    mostrarTotalClientes();
                    break;
                case 9:
                    mostrarMediaIdadesClientes();
                    break;
                case 10:
                    mostrarEmailDominioPercentagem();
                    break;
                case 11:
                    mostrarMediaAvaliacoes();
                    break;
                case 0:
                    System.out.println("A voltar para o menu de Admin...");
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private void mostrarClienteComMaisLancesPorValor() {
        System.out.println("\n=== Leilões ===");
        List<Leilao> todosLeiloes = LeilaoController.listarLeiloes();

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
        System.out.print("ID do leilão (0 para retornar): ");
        int id = scanner.nextInt();
        if(id == 0){
            return;
        }
        Leilao leilaoSelecionado = leilaoController.buscarLeilaoPorId(id);

        if (leilaoSelecionado != null) {
            Cliente cliente = estatisticaController.ClienteMaisLancesPorQuantidadeEValor(leilaoSelecionado);
            if (cliente != null) {
                System.out.println("Cliente com mais lances: " + cliente.getNome() + " - " + cliente.getEmail());
            } else {
                System.out.println("Nenhum lance neste leilão.");
            }
        } else {
            System.out.println("Leilão não encontrado.");
        }
    }

    private void mostrarTempoAtivoDeLeilao() {
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
        System.out.print("ID do leilão (0 para retornar): ");
        int id = scanner.nextInt();
        if(id == 0){
            return;
        }
        Leilao leilaoSelecionado = leilaoController.buscarLeilaoPorId(id);

        if (leilaoSelecionado != null) {
            long tempo = estatisticaController.getTempoAtivoLeilao(leilaoSelecionado);
            System.out.println("Tempo total ativo: " + tempo + " dia(s)");
        } else {
            System.out.println("Leilão não encontrado.");
        }
    }

    private void mostrarQuantidadeLeiloesTerminados() {
        int total = estatisticaController.getQuantidadeLeiloesTerminados();
        System.out.println("Quantidade de leilões terminados: " + total);
    }

    private void mostrarLeilaoComMaisTempoAtivo() {
        Leilao leilao = estatisticaController.getLeilaoMaisTempoAtivo();
        if (leilao != null) {
            System.out.println("Leilão com mais tempo ativo: " + leilao.getNomeProduto());
        } else {
            System.out.println("Nenhum leilão encontrado.");
        }
    }

    private void mostrarLeilaoComMaisLances() {
        Leilao leilao = estatisticaController.getLeilaoMaisLances();
        if (leilao != null) {
            System.out.println("Leilão com mais lances: " + leilao.getNomeProduto());
        } else {
            System.out.println("Nenhum leilão encontrado.");
        }
    }

    public void mostrarEstatisticasTempoLances() {
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n📊 ESTATÍSTICAS DE TEMPO ENTRE LANCES");
            System.out.println("1. Ver média geral");
            System.out.println("2. Ver média por leilão");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            if (opcao == 1) {
                mostrarMediaGeral();
            }
            else if (opcao == 2) {
                mostrarMediaPorLeilao();
            }
            else if (opcao == 0) {
                System.out.println("Retornando...");
            }
            else {
                System.out.println("Opção inválida!");
            }
        }
    }

    private void mostrarMediaGeral() {
        double media = estatisticaController.calcularMediaTempoAtePrimeiroLance();
        System.out.printf("\n Média geral: %.1f minutos\n", media);
    }

    private void mostrarMediaPorLeilao() {
        System.out.println("\nEscolha o leilão:");

        for (Leilao leilao : leilaoController.getLeiloes()) {
            System.out.printf("ID %d - %s\n",
                    leilao.getId(),
                    leilao.getNomeProduto());
        }

        System.out.print("\nDigite o ID do leilão (ou 0 para cancelar): ");
        int idEscolhido = scanner.nextInt();
        scanner.nextLine();

        if (idEscolhido == 0) return;

        for (Leilao leilao : leilaoController.getLeiloes()) {
            if (leilao.getId() == idEscolhido) {
                double media = estatisticaController.calcularMediaTempoPorLeilao(leilao);
                System.out.printf("\n⏱Média no leilão '%s': %.1f minutos\n",
                        leilao.getNomeProduto(),
                        media);
                return;
            }
        }

        System.out.println("Leilão não encontrado!");
    }


    private void mostrarLeiloesSemLances() {
        int total = estatisticaController.getLeiloesSemLances();
        System.out.println("Leilões sem lances: " + total);
    }

    private void mostrarTotalClientes() {
        int total = estatisticaController.TotalClientesRegistados();
        System.out.println("Clientes registados: " + total);
    }

    private void mostrarMediaIdadesClientes() {
        double media = estatisticaController.MediaIdadesClientes();
        System.out.printf("Média de idades dos clientes: %.2f anos\n", media);
    }

    public void mostrarEmailDominioPercentagem() {
        String dominio = EstatisticaController.obterDominioMaisUtilizado();
        double percentagem = EstatisticaController.calcularPercentagemMaiorDominioEmail();

        System.out.printf("Domínio mais usado: %s (%.2f%% dos clientes)%n", dominio, percentagem);
    }

    public static void mostrarMediaAvaliacoes() {
        Scanner scanner = new Scanner(System.in);
        List<AvaliacaoLeilao> avaliacoes = EstatisticaController.listarAvaliacoes();
        List<Leilao> leiloes = LeilaoController.listarLeiloes();

        System.out.println("\nEstatísticas de Avaliações");
        System.out.println("-----------------------------");
        System.out.println("1. Média Geral de Todos os Leilões");
        System.out.println("2. Média por Leilão");
        System.out.print("\nEscolha uma opção: ");

        int opcao = scanner.nextInt();

        if (opcao == 1) {
            double mediaGeral = EstatisticaController.calcularMediaClassificacoes(avaliacoes);
            System.out.println("\nMédia geral das avaliações");
            System.out.println("-----------------------------");
            System.out.printf("Média de todos os leilões: %.1f/5\n", mediaGeral);
        }
        else if (opcao == 2) {
            if (leiloes.isEmpty()) {
                System.out.println("\nNão existem leilões registados.");
                return;
            }

            System.out.println("\nLeilões");
            System.out.println("-----------------------------");
            for (Leilao leilao : leiloes) {
                System.out.printf("%d. %s\n",
                        leilao.getId(),
                        leilao.getNomeProduto()
                );
            }

            System.out.print("\nEscolha o ID do leilão: ");
            int idLeilao = scanner.nextInt();

            Leilao leilaoSelecionado = null;
            for (Leilao leilao : leiloes) {
                if (leilao.getId() == idLeilao) {
                    leilaoSelecionado = leilao;
                    break;
                }
            }

            if (leilaoSelecionado != null) {
                double mediaLeilao = EstatisticaController.calcularMediaClassificacoesPorLeilao(avaliacoes, leilaoSelecionado);
                System.out.println("\nMédia do Leilão");
                System.out.println("-----------------------------");
                System.out.printf("Leilão: %s\n", leilaoSelecionado.getNomeProduto());
                System.out.printf("Média das avaliações: %.1f/5\n", mediaLeilao);
            } else {
                System.out.println("\nLeilão não encontrado!");
            }
        }
    }



}
