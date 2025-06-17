package Controller;

import Data.AvaliacaoLeilaoData;
import Data.ClienteData;
import Data.LeilaoData;
import Data.NotaData;
import Model.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class EstatisticaController {
    private final ClienteController clienteController;
    private final LeilaoController leilaoController;

    public EstatisticaController(ClienteController clienteController, LeilaoController leilaoController) {
        this.clienteController = clienteController;
        this.leilaoController = leilaoController;
    }



    public Cliente ClienteMaisLancesPorQuantidadeEValor(Leilao leilao) {
        List<Lance> lances = leilaoController.getLances();
        java.util.Map<Cliente, Integer> quantidadePorCliente = new java.util.HashMap<>();
        java.util.Map<Cliente, Double> valorPorCliente = new java.util.HashMap<>();

        for (Lance lance : lances) {
            if (lance.getLeilao() != null && lance.getLeilao().getId() == leilao.getId()) {
                Cliente cliente = lance.getCliente();
                quantidadePorCliente.put(cliente, quantidadePorCliente.getOrDefault(cliente, 0) + 1);
                valorPorCliente.put(cliente, valorPorCliente.getOrDefault(cliente, 0.0) + lance.getValor());
            }
        }

        Cliente clienteTop = null;
        int maxQuantidade = 0;
        double maxValor = 0;

        for (Cliente cliente : quantidadePorCliente.keySet()) {
            int quantidade = quantidadePorCliente.get(cliente);
            double valor = valorPorCliente.get(cliente);
            if (quantidade > maxQuantidade || (quantidade == maxQuantidade && valor > maxValor)) {
                maxQuantidade = quantidade;
                maxValor = valor;
                clienteTop = cliente;
            }
        }
        return clienteTop;
    }


    public long getTempoAtivoLeilao(Leilao leilao) {
        if (leilao == null || leilao.getDataInicio() == null || leilao.getDataFim() == null) {
            return 0;
        }

        List<Lance> lances = leilaoController.getLances();
        LocalDateTime inicio = leilao.getDataInicio();
        LocalDateTime fimReal;

        if (leilao instanceof LeilaoVendaDireta) {
            Lance lanceVendaDireta = null;
            for (Lance lance : lances) {
                if (lance.getLeilao() != null && lance.getLeilao().getId() == leilao.getId()) {
                    lanceVendaDireta = lance;
                    break;
                }
            }

            if (lanceVendaDireta != null) {
                fimReal = lanceVendaDireta.getDataHora();
            } else {
                LocalDateTime agora = LocalDateTime.now();
                fimReal = agora.isAfter(leilao.getDataFim()) ? leilao.getDataFim() : agora;
            }
        } else {
            // Para outros tipos de leilão
            LocalDateTime agora = LocalDateTime.now();
            fimReal = agora.isAfter(leilao.getDataFim()) ? leilao.getDataFim() : agora;
        }


        return ChronoUnit.DAYS.between(inicio.toLocalDate(), fimReal.toLocalDate()) + 1;
    }

    public int getQuantidadeLeiloesTerminados() {
        List<Leilao> leiloes = leilaoController.getLeiloes();
        int count = 0;
        LocalDateTime agora = LocalDateTime.now();

        for (Leilao leilao : leiloes) {
            if (leilao instanceof LeilaoVendaDireta && leilao.isFechado()) {
                count++;
            }
            else if (leilao.getDataFim() != null && leilao.getDataFim().isBefore(agora) && leilao.isFechado()) {
                count++;
            }
        }
        return count;
    }


    public Leilao getLeilaoMaisTempoAtivo() {
        List<Leilao> leiloes = leilaoController.getLeiloes();
        long maiorTempo = 0;
        Leilao resultado = null;

        for (Leilao leilao : leiloes) {
            long tempo = getTempoAtivoLeilao(leilao);
            if (tempo > maiorTempo) {
                maiorTempo = tempo;
                resultado = leilao;
            }
        }

        return resultado;
    }

    public Leilao getLeilaoMaisLances() {
        List<Leilao> leiloes = leilaoController.getLeiloes();
        List<Lance> lances = leilaoController.getLances();
        int maxLances = 0;
        Leilao leilaoComMaisLances = null;

        for (Leilao leilao : leiloes) {
            int contador = 0;
            for (Lance lance : lances) {
                if (lance.getLeilao() != null && lance.getLeilao().getId() == leilao.getId()) {
                    contador++;
                }
            }
            if (contador > maxLances) {
                maxLances = contador;
                leilaoComMaisLances = leilao;
            }
        }
        return (maxLances > 0) ? leilaoComMaisLances : null;
    }


    public double getMediaTempoAteLance() {
        List<Lance> lances = leilaoController.getLances();
        if (lances.isEmpty()) return 0;
        long soma = 0;
        int total = 0;

        Map<Integer, List<Lance>> lancesPorLeilao = new HashMap<>();
        for (Lance lance : lances) {
            if (lance.getLeilao() != null) {
                lancesPorLeilao.computeIfAbsent(lance.getLeilao().getId(), k -> new ArrayList<>()).add(lance);
            }
        }

        for (List<Lance> listaLances : lancesPorLeilao.values()) {
            listaLances.sort(Comparator.comparing(Lance::getDataHora));
            LocalDateTime inicioLeilao = listaLances.get(0).getLeilao().getDataInicio(); // Já é LocalDateTime
            if (inicioLeilao == null) continue;

            for (Lance lance : listaLances) {
                LocalDateTime momento = lance.getDataHora();
                if (momento != null) {
                    Duration duration = Duration.between(inicioLeilao, momento);
                    soma += duration.toMinutes();
                    total++;
                }
            }
        }
        return total == 0 ? 0 : soma / (double) total;
    }

    public int getLeiloesSemLances() {
        List<Leilao> leiloes = leilaoController.getLeiloes();
        List<Lance> lances = leilaoController.getLances();
        int count = 0;

        for (Leilao leilao : leiloes) {
            boolean temLance = false;
            for (Lance lance : lances) {
                if (lance.getLeilao() != null && lance.getLeilao().getId() == leilao.getId()) {
                    temLance = true;
                    break;
                }
            }
            if (!temLance) {
                count++;
            }
        }
        return count;
    }

    public int TotalClientesRegistados() {
        List<Cliente> clientes = ClienteController.listarClientes();
        return clientes.size();
    }

    public double MediaIdadesClientes() {
        List<Cliente> clientes = ClienteController.listarClientes();
        int somaIdades = 0;
        int totalClientes = 0;

        for (Cliente cliente : clientes) {
            LocalDate dataNascimento = cliente.getDataNascimento();

            if (dataNascimento != null) {
                int idade = Period.between(dataNascimento, LocalDate.now()).getYears();
                somaIdades += idade;
                totalClientes++;
            }
        }

        if (totalClientes == 0) {
            return 0;
        }
        return (double) somaIdades / totalClientes;
    }

    public double calcularMediaTempoAtePrimeiroLance() {
        List<Lance> lances = leilaoController.getLances();
        if (lances.isEmpty()) return 0;

        long somaMinutos = 0;
        int leiloesComLances = 0;

        for (Leilao leilao : leilaoController.getLeiloes()) {
            double mediaLeilao = calcularMediaTempoPorLeilao(leilao);
            if (mediaLeilao > 0) {
                somaMinutos += (long) mediaLeilao;
                leiloesComLances++;
            }
        }

        return leiloesComLances > 0 ? (double) somaMinutos / leiloesComLances : 0;
    }

    public double calcularMediaTempoPorLeilao(Leilao leilao) {
        if (leilao == null || leilao.getDataInicio() == null) return 0;

        List<Lance> lancesLeilao = leilaoController.getLances().stream()
                .filter(l -> l.getLeilao() != null && l.getLeilao().getId() == leilao.getId())
                .sorted(Comparator.comparing(Lance::getDataHora))
                .toList();

        if (lancesLeilao.isEmpty()) return 0;

        long somaMinutos = 0;
        LocalDateTime inicioLeilao = leilao.getDataInicio();

        for (Lance lance : lancesLeilao) {
            Duration duracao = Duration.between(inicioLeilao, lance.getDataHora());
            somaMinutos += duracao.toMinutes();
        }

        return (double) somaMinutos / lancesLeilao.size();
    }

    public static String obterDominioMaisUtilizado() {
        List<Cliente> clientes = ClienteData.carregarClientes();
        List<String> dominios = new ArrayList<>();
        List<Integer> contagens = new ArrayList<>();

        for (Cliente cliente : clientes) {
            String email = cliente.getEmail();
            int arrobaIndex = email.lastIndexOf('@');

            if (arrobaIndex != -1 && arrobaIndex < email.length() - 1) {
                String dominio = email.substring(arrobaIndex + 1).toLowerCase();

                int index = dominios.indexOf(dominio);

                if (index != -1) {
                    contagens.set(index, contagens.get(index) + 1);
                } else {

                    dominios.add(dominio);
                    contagens.add(1);
                }
            }
        }

        String dominioMaisUsado = "";
        int maxContagem = 0;

        for (int i = 0; i < dominios.size(); i++) {
            if (contagens.get(i) > maxContagem) {
                maxContagem = contagens.get(i);
                dominioMaisUsado = dominios.get(i);
            }
        }

        return dominioMaisUsado;
    }


    public static List<AvaliacaoLeilao> listarAvaliacoes() {
        List<Cliente> clientes = ClienteController.listarClientes();
        List<Leilao> leiloes = LeilaoController.listarLeiloes();
        List<Nota> notas = NotaData.carregarNotas();


        return AvaliacaoLeilaoData.carregarAvaliacoes(clientes, leiloes, notas);
    }


    public static double calcularPercentagemMaiorDominioEmail() {
        List<Cliente> clientes = ClienteData.carregarClientes();
        int total = clientes.size();

        if (total == 0) return 0;

        String dominioMaisUsado = obterDominioMaisUtilizado();
        int contador = 0;

        for (Cliente cliente : clientes) {
            String email = cliente.getEmail().toLowerCase();
            if (email.endsWith("@" + dominioMaisUsado)) {
                contador++;
            }
        }

        return ((double) contador / total) * 100;
    }


    public static double calcularMediaClassificacoes(List<AvaliacaoLeilao> avaliacoes) {
        if (avaliacoes == null || avaliacoes.isEmpty()) {
            return 0;
        }

        double soma = 0;
        int count = 0;

        for (AvaliacaoLeilao avaliacao : avaliacoes) {
            soma += avaliacao.getNota().getId();
            count++;
        }

        return count > 0 ? soma / count : 0;
    }

    public static double calcularMediaClassificacoesPorLeilao(List<AvaliacaoLeilao> avaliacoes, Leilao leilao) {
        if (avaliacoes == null || avaliacoes.isEmpty() || leilao == null) {
            return 0;
        }

        double soma = 0;
        int count = 0;

        for (AvaliacaoLeilao avaliacao : avaliacoes) {
            if (avaliacao.getLeilao().getId() == leilao.getId()) {
                soma += avaliacao.getNota().getId();
                count++;
            }
        }

        return count > 0 ? soma / count : 0;
    }

}

