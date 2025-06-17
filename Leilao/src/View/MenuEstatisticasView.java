package View;

import Model.Estatisticas;
import Model.Leilao;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Scanner;

public class MenuEstatisticasView {

    private final List<Leilao> leiloes;

    // Construtor da classe para receber a lista de leilões
    public MenuEstatisticasView(List<Leilao> leiloes) {
        this.leiloes = leiloes;
    }

    public void mostrarMenu() {  // Removi o static, pois é instância de um objeto
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("\n====== MENU DE ESTATÍSTICAS ======");
            System.out.println("1. Quantidade de clientes registados");
            System.out.println("2. Média de idades dos clientes");
            System.out.println("3. Quantidade de leilões terminados");
            System.out.println("4. Leilão com mais tempo ativo");
            System.out.println("5. Sair");
            System.out.print("Escolha uma opção: ");

            while (!scanner.hasNextInt()) {
                System.out.print("Por favor, insira um número válido: ");
                scanner.next();
            }

            opcao = scanner.nextInt();
            scanner.nextLine(); // limpar buffer

            switch (opcao) {
                case 1:
                    int totalClientes = Estatisticas.contarClientesRegistados();
                    System.out.println("Total de clientes registados: " + totalClientes);
                    break;

                case 2:
                    int mediaIdade = Estatisticas.calcularMediaIdades();
                    System.out.println("Média de idades dos clientes: " + mediaIdade + " anos");
                    break;

                case 3:
                    int leiloesTerminados = Estatisticas.quantidadeLeiloesTerminados(leiloes);
                    System.out.println("Leilões terminados: " + leiloesTerminados);
                    break;

                case 4:
                    Leilao maisTempo = Estatisticas.leilaoMaisTempoAtivo(leiloes);  // Usando a lista de leilões passada ao invés de LeilaoController
                    if (maisTempo != null) {
                        long dias = ChronoUnit.DAYS.between(maisTempo.getDataInicio(), maisTempo.getDataFim());
                        System.out.println("Leilão com mais tempo ativo:");
                        System.out.println("→ Produto: " + maisTempo.getNomeProduto());
                        System.out.println("→ Duração: " + dias + " dias");
                    } else {
                        System.out.println("Nenhum leilão encontrado.");
                    }
                    break;

                case 5:
                    Estatisticas.percentagemDominioMaisUsado();
                    break;

                case 6:
                    System.out.println("A sair do menu de estatísticas...");
                    break;

                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }

        } while (opcao != 5);
    }
}
