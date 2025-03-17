package Controller;

import Model.Cliente;
import Model.Estatisticas;
import Model.Leilao;
import View.EstatisticasView;
import View.MenuEstatisticasView;

public class EstatisticasController {
    private Estatisticas estatisticas;
    private EstatisticasView visao;

    public EstatisticasController(Estatisticas estatisticas, EstatisticasView visao) {
        this.estatisticas = estatisticas;
        this.visao = visao;
    }

    public void exibirEstatisticasLeilao(Leilao leilao) {
        Cliente clienteMaiorLance = estatisticas.obterMaiorLance(leilao);
        long tempoAtivo = estatisticas.tempoAtivo(leilao);
        visao.mostrarEstatisticasLeilao(leilao, clienteMaiorLance, tempoAtivo);
    }

    public void exibirEstatisticasGlobais() {
        long totalTerminados = estatisticas.totalLeiloesTerminados();
        Leilao leilaoMaiorTempo = estatisticas.leilaoMaiorTempoAtivo();
        Leilao leilaoMaisLances = estatisticas.leilaoMaisLances();
        double mediaTempo = estatisticas.mediaTempoParaLance();
        int totalClientes = estatisticas.totalClientes();
        double mediaIdades = estatisticas.mediaIdades();
        double percentagemDominio = estatisticas.percentagemDominioEmailMaisUsado();
        visao.mostrarEstatisticasGlobais(totalTerminados, leilaoMaiorTempo, leilaoMaisLances,
                mediaTempo, totalClientes, mediaIdades, percentagemDominio);
    }
}
