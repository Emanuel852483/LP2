package Model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class Estatisticas {
    private List<Leilao> leiloes;
    private List<Cliente> clientes;

    public Estatisticas(List<Leilao> leiloes, List<Cliente> clientes) {
        this.leiloes = leiloes;
        this.clientes = clientes;
    }

    // Cliente com o maior lance em um leilão
    public Cliente obterMaiorLance(Leilao leilao) {
        Optional<Lance> maior = leilao.getLances().stream()
                .max((l1, l2) -> Double.compare(l1.getValor(), l2.getValor()));
        return maior.map(Lance::getCliente).orElse(null);
    }

    // Tempo ativo do leilão em horas
    public long tempoAtivo(Leilao leilao) {
        if(leilao.getDataFim() != null) {
            Duration duracao = Duration.between(leilao.getDataInicio(), leilao.getDataFim());
            return duracao.toHours();
        }
        return 0;
    }

    // Total de leilões terminados (dataFim anterior ao agora)
    public long totalLeiloesTerminados() {
        LocalDateTime agora = LocalDateTime.now();
        return leiloes.stream().filter(l -> l.getDataFim() != null && l.getDataFim().isBefore(agora)).count();
    }

    // Leilão com maior tempo ativo
    public Leilao leilaoMaiorTempoAtivo() {
        return leiloes.stream()
                .filter(l -> l.getDataFim() != null)
                .max((l1, l2) -> Long.compare(tempoAtivo(l1), tempoAtivo(l2)))
                .orElse(null);
    }

    // Leilão com mais lances
    public Leilao leilaoMaisLances() {
        return leiloes.stream()
                .max((l1, l2) -> Integer.compare(l1.getLances().size(), l2.getLances().size()))
                .orElse(null);
    }

    // Média de tempo (em minutos) para um lance acontecer
    public double mediaTempoParaLance() {
        long totalMinutos = 0;
        int contador = 0;
        for(Leilao leilao : leiloes) {
            for(Lance lance : leilao.getLances()) {
                Duration duracao = Duration.between(leilao.getDataInicio(), lance.getDataHora());
                totalMinutos += duracao.toMinutes();
                contador++;
            }
        }
        return contador > 0 ? (double) totalMinutos / contador : 0;
    }

    // Leilões sem lances
    public List<Leilao> leiloesSemLances() {
        return leiloes.stream().filter(l -> l.getLances().isEmpty()).toList();
    }

    // Total de clientes
    public int totalClientes() {
        return clientes.size();
    }

    // Média de idades dos clientes
    public double mediaIdades() {
        return clientes.stream().mapToInt(Cliente::getIdade).average().orElse(0);
    }

    // Percentagem de clientes que usam o domínio de e-mail mais comum
    public double percentagemDominioEmailMaisUsado() {
        Map<String, Integer> dominios = new HashMap<>();
        for(Cliente c : clientes) {
            String email = c.getEmail();
            String[] partes = email.split("@");
            if(partes.length == 2) {
                String dominio = partes[1];
                dominios.put(dominio, dominios.getOrDefault(dominio, 0) + 1);
            }
        }
        if(dominios.isEmpty()) return 0;
        int max = dominios.values().stream().max(Integer::compare).orElse(0);
        return ((double) max / clientes.size()) * 100;
    }

    public abstract void mostrarEstatisticasLeilao(Leilao leilao, Cliente clienteMaiorLance, long tempoAtivo);

    public abstract void mostrarEstatisticasGlobais(long totalTerminados, Leilao leilaoMaiorTempo, Leilao leilaoMaisLances,
                                                    double mediaTempo, int totalClientes, double mediaIdades, double percentagemDominio);
}

