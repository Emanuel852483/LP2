package View;

import Controller.AgenteController;
import Model.Cliente;
import Model.Leilao;
import Model.LeilaoEletronico;

import java.util.List;
import java.util.Scanner;

public class AgenteView {
    private final AgenteController agenteController;
    private final Scanner scanner;

    public AgenteView(AgenteController agenteController) {
        this.agenteController = agenteController;
        this.scanner = new Scanner(System.in);
    }

    public void menuConfigurarAgente(Cliente cliente) {
        while (true) {
            List<Leilao> leiloesEletronicos = agenteController.listarLeiloesEletronicosAtivosDoCliente(cliente);

            if (leiloesEletronicos.isEmpty()) {
                System.out.println("\nNão existem leilões eletrônicos ativos em que você esteja inscrito.");
                return;
            }

            System.out.println("\n=== Configuração de Agente ===");
            System.out.println("Leilões Eletrônicos Disponíveis:");

            for (int i = 0; i < leiloesEletronicos.size(); i++) {
                Leilao leilao = leiloesEletronicos.get(i);
                System.out.printf("%d. %s (Valor Mínimo: %.2f, Múltiplo Lance: %.2f)\n",
                        i + 1,
                        leilao.getNomeProduto(),
                        leilao.getValorMinimo(),
                        ((LeilaoEletronico) leilao).getMultiploLance());
            }

            System.out.println("0. Voltar");
            System.out.print("\nEscolha o leilão para configurar o agente (0 para voltar): ");

            int escolha;
            try {
                escolha = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("\nOpção inválida! Por favor, digite um número.");
                continue;
            }

            if (escolha == 0) {
                return;
            }

            if (escolha < 1 || escolha > leiloesEletronicos.size()) {
                System.out.println("\nLeilão inválido! Por favor, escolha um leilão válido.");
                continue;
            }

            LeilaoEletronico leilaoEscolhido = (LeilaoEletronico) leiloesEletronicos.get(escolha - 1);
            configurarAgenteParaLeilao(cliente, leilaoEscolhido);
        }
    }

    private void configurarAgenteParaLeilao(Cliente cliente, LeilaoEletronico leilao) {
        System.out.println("\n=== Configurar Agente para " + leilao.getNomeProduto() + " ===");

        if (agenteController.temAgenteAtivo(cliente, leilao)) {
            System.out.println("\nVocê já tem um agente ativo para este leilão!");
            System.out.println("Aguarde até que seu agente atual seja desativado (atingir valor máximo ou saldo insuficiente).");
            return;
        }

        double proximoLance;
        if (leilao.getLances().isEmpty()) {
            proximoLance = leilao.getValorMinimo() + leilao.getMultiploLance();
        } else {
            proximoLance = leilao.getLances().getLast().getValor() + leilao.getMultiploLance();
        }

        System.out.println("Próximo lance será: " + proximoLance);
        System.out.println("Múltiplo do lance: " + leilao.getMultiploLance());
        System.out.println("Seu saldo atual: " + cliente.getSaldo());

        while (true) {
            System.out.print("\nDigite o valor máximo para o agente (0 para cancelar): ");

            double valorMaximo;
            try {
                valorMaximo = Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("\nValor inválido! Por favor, digite um número válido.");
                continue;
            }

            if (valorMaximo == 0) {
                return;
            }

            if (valorMaximo < proximoLance) {
                System.out.println("\nO valor máximo deve ser maior ou igual ao próximo lance!");
                continue;
            }

            double diferencaValor = valorMaximo - (leilao.getLances().isEmpty() ?
                    leilao.getValorMinimo() : leilao.getLances().getLast().getValor());

            if (diferencaValor % leilao.getMultiploLance() != 0) {
                System.out.println("\nO valor máximo deve respeitar o múltiplo de lance do leilão!");
                continue;
            }

            if (valorMaximo > cliente.getSaldo()) {
                System.out.println("\nO valor máximo não pode ser maior que seu saldo atual!");
                continue;
            }

            agenteController.criarAgente(cliente, leilao, valorMaximo);
            System.out.println("\nAgente configurado com sucesso!");
            return;
        }
    }
}