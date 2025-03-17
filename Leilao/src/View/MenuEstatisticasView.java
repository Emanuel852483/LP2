package View;

import Model.Cliente;
import Model.Estatisticas;
import Model.Leilao;

import java.util.List;

public class MenuEstatisticasView extends Estatisticas {

    public MenuEstatisticasView(List<Leilao> leiloes, List<Cliente> clientes) {
        super(leiloes, clientes);
    }


    @Override
    public void mostrarEstatisticasLeilao(Leilao leilao, Cliente clienteMaiorLance, long tempoAtivo) {
        System.out.println("Estatísticas do Leilão: " + leilao.getNomeProduto());
        if(clienteMaiorLance != null) {
            System.out.println("Cliente com maior lance: " + clienteMaiorLance.getNome());
        } else {
            System.out.println("Nenhum lance realizado.");
        }
        System.out.println("Tempo ativo (horas): " + tempoAtivo);
        System.out.println("-----------------------------");
    }

    @Override
    public void mostrarEstatisticasGlobais(long totalTerminados, Leilao leilaoMaiorTempo, Leilao leilaoMaisLances,
                                           double mediaTempo, int totalClientes, double mediaIdades, double percentagemDominio) {
        System.out.println("Estatísticas Globais:");
        System.out.println("Total de leilões terminados: " + totalTerminados);
        if(leilaoMaiorTempo != null) {
            System.out.println("Leilão com maior tempo ativo: " + leilaoMaiorTempo.getNomeProduto());
        }
        if(leilaoMaisLances != null) {
            System.out.println("Leilão com mais lances: " + leilaoMaisLances.getNomeProduto());
        }
        System.out.println("Média de tempo para lance (minutos): " + mediaTempo);
        System.out.println("Total de clientes: " + totalClientes);
        System.out.println("Média de idades: " + mediaIdades);
        System.out.println("Percentagem do domínio de e-mail mais usado: " + percentagemDominio + "%");
    }
}
