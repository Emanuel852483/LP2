package Controller;

import Model.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class AgenteController {
    private final List<Agente> agentes;
    private final LeilaoController leilaoController;
    private final ExecutorService executorService;

    public AgenteController(LeilaoController leilaoController) {
        this.agentes = Collections.synchronizedList(new ArrayList<>());
        this.leilaoController = leilaoController;
        this.executorService = Executors.newFixedThreadPool(5);
    }

    public List<Leilao> listarLeiloesEletronicosAtivosDoCliente(Cliente cliente) {
        List<Leilao> leiloesEletronicosAtivos = new ArrayList<>();
        List<Leilao> leiloesAtivos = leilaoController.listarLeiloesAtivos();

        for (Leilao leilao : leiloesAtivos) {
            if (leilao instanceof LeilaoEletronico && leilao.getClientesInscritos().contains(cliente)) {
                leiloesEletronicosAtivos.add(leilao);
            }
        }
        return leiloesEletronicosAtivos;
    }

    public boolean temAgenteAtivo(Cliente cliente, LeilaoEletronico leilao) {
        synchronized (agentes) {
            for (Agente agente : agentes) {
                if (agente.getCliente().equals(cliente) &&
                        agente.getLeilao().equals(leilao) &&
                        agente.isAtivo()) {
                    if (!leilao.getLances().isEmpty() &&
                            leilao.getLances().getLast().getValor() >= agente.getValorMaximo()) {
                        agente.setAtivo(false);
                        return false;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public void criarAgente(Cliente cliente, LeilaoEletronico leilao, double valorMaximo) {
        agentes.removeIf(a -> a.getCliente().equals(cliente) && a.getLeilao().equals(leilao));

        Agente novoAgente = new Agente(cliente, leilao, valorMaximo, leilao.getMultiploLance());
        agentes.add(novoAgente);

        executorService.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
                synchronized (leilao) {  // Bloqueio no objeto leilão
                    double valorLance;
                    if (leilao.getLances().isEmpty()) {
                        valorLance = leilao.getValorMinimo() + leilao.getMultiploLance();
                    } else {
                        Lance ultimoLance = leilao.getLances().getLast();
                        valorLance = ultimoLance.getValor() + leilao.getMultiploLance();

                        if (ultimoLance.getCliente().equals(cliente)) {
                            return;
                        }
                    }

                    if (valorLance <= valorMaximo && cliente.getSaldo() >= valorLance) {
                        leilaoController.registrarLance(leilao, cliente, valorLance);
                    } else {
                        novoAgente.setAtivo(false);
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    public void processarNovoLance(Lance lance) {
        if (!(lance.getLeilao() instanceof LeilaoEletronico)) return;

        LeilaoEletronico leilao = (LeilaoEletronico) lance.getLeilao();
        List<Agente> agentesAtivos = new ArrayList<>();

        synchronized (agentes) {
            for (Agente agente : agentes) {
                if (agente.getLeilao().equals(leilao) &&
                        agente.isAtivo() &&
                        !agente.getCliente().equals(lance.getCliente())) {
                    agentesAtivos.add(agente);
                }
            }

            agentesAtivos.sort(Comparator.comparing(Agente::getDataCriacao));
        }

        for (int i = 0; i < agentesAtivos.size(); i++) {
            final int delaySeconds = (i + 1) * 5;
            Agente agente = agentesAtivos.get(i);

            executorService.submit(() -> {
                try {
                    TimeUnit.SECONDS.sleep(delaySeconds);
                    synchronized (leilao) {
                        realizarLanceAutomatico(agente, lance);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
    }

    private void realizarLanceAutomatico(Agente agente, Lance ultimoLance) {
        LeilaoEletronico leilao = (LeilaoEletronico) agente.getLeilao();

        Lance atualUltimoLance = leilao.getLances().isEmpty() ? null : leilao.getLances().getLast();
        if (atualUltimoLance != null &&
                (atualUltimoLance.getValor() != ultimoLance.getValor() ||
                        atualUltimoLance.getCliente().equals(agente.getCliente()))) {
            return;
        }

        double novoValor = ultimoLance.getValor() + leilao.getMultiploLance();

        if (novoValor > agente.getValorMaximo() ||
                agente.getCliente().getSaldo() < novoValor) {
            agente.setAtivo(false);
            return;
        }

        leilaoController.registrarLance(leilao, agente.getCliente(), novoValor);
    }
}

