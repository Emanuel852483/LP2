package Controller;

import Model.*;
import Data.LeilaoData;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LeilaoController {
    private List<Leilao> leiloes;

    // Construtor
    public LeilaoController() {
        this.leiloes = new ArrayList<>();
    }

    public static List<Leilao> getLeiloes() {
        return List.of();
    }

    public void setLeiloes(List<Leilao> leiloes) {
        this.leiloes = leiloes;
    }

    // Método para adicionar um leilão
    public boolean adicionarLeilao(Leilao leilao) {
        return leiloes.add(leilao);
    }

    // Método para remover um leilão
    public boolean removerLeilao(int id) {
        Leilao leilao = buscarLeilaoPorId(id);
        if(leilao != null){
            leiloes.remove(leilao);
            return true;
        }
        return false;
    }


    // Método para buscar um leilão pelo id
    public Leilao buscarLeilaoPorId(int id) {
        for (Leilao leilao : leiloes) {
            if (leilao.getId() == id) {
                return leilao;
            }
        }
        return null;
    }

    // Método para listar todos os leilões
    public List<Leilao> listarLeiloes()
    {
        return leiloes;
    }

    // Método para criar um leilão eletrônico
    public LeilaoEletronico criarLeilaoEletronico(String nomeProduto, String descricao, LocalDate dataInicio, LocalDate dataFim, double valorMinimo, double multiploLance, boolean isAtivo, boolean isFechado) {
        return new LeilaoEletronico(nomeProduto, descricao, dataInicio, dataFim, valorMinimo, multiploLance, isAtivo, isFechado);
    }

    // Método para criar um leilão carta fechada
    public LeilaoCartaFechada criarLeilaoCartaFechada(String nomeProduto, String descricao, LocalDate dataInicio, LocalDate dataFim, double valorMinimo, boolean isAtivo, boolean isFechado) {
        return new LeilaoCartaFechada(nomeProduto, descricao, dataInicio, dataFim, valorMinimo, isAtivo, isFechado);
    }

    // Método para criar um leilão venda direta
    public LeilaoVendaDireta criarLeilaoVendaDireta(String nomeProduto, String descricao, LocalDate dataInicio, LocalDate dataFim, double valorMinimo, boolean isAtivo, boolean isFechado) {
        return new LeilaoVendaDireta(nomeProduto, descricao, dataInicio, dataFim, valorMinimo, isAtivo, isFechado);


    }

    public void verificarStatusLeiloes(List<Leilao> leiloes) {
        LocalDate hoje = LocalDate.now();
        boolean modificado = false;

        for (Leilao leilao : leiloes) {
            if (leilao instanceof LeilaoVendaDireta && leilao.isFechado()) continue;

            boolean deveriaEstarAtivo = !hoje.isBefore(leilao.getDataInicio()) &&
                    !hoje.isAfter(leilao.getDataFim());
            boolean deveriaEstarFechado = hoje.isAfter(leilao.getDataFim());

            boolean foiFechadoAgora = !leilao.isFechado() && deveriaEstarFechado;

            leilao.setAtivo(deveriaEstarAtivo && !deveriaEstarFechado);
            leilao.setFechado(deveriaEstarFechado);

            if (foiFechadoAgora) {
                modificado = true;

                // Enviar e-mail ao vencedor, se houver
                if (!leilao.getLances().isEmpty()) {
                    Lance lanceVencedor = leilao.getLances().stream()
                            .max((l1, l2) -> Double.compare(l1.getValor(), l2.getValor()))
                            .orElse(null);

                    if (lanceVencedor != null) {
                        Cliente vencedor = lanceVencedor.getCliente();
                        NotificacaoController.enviarEmailVencedorLeilao(
                                vencedor.getEmail(),
                                vencedor.getNome(),
                                leilao.getNomeProduto(),
                                lanceVencedor.getValor()
                        );
                    }
                }
            }
        }

        if (modificado) {
            LeilaoData.salvarLeiloes(leiloes);
        }
    }





    public boolean registrarLance(Leilao leilao, Cliente cliente, double valor) {
        if (leilao == null || cliente == null) {
            return false;
        }

        if (leilao instanceof LeilaoEletronico) {
            return registrarLanceEletronico((LeilaoEletronico) leilao, cliente, valor);
        } else if (leilao instanceof LeilaoCartaFechada) {
            return registrarLanceCartaFechada((LeilaoCartaFechada) leilao, cliente, valor);
        } else if (leilao instanceof LeilaoVendaDireta) {
            return registrarLanceVendaDireta((LeilaoVendaDireta) leilao, cliente, valor);
        } else {
            return false;
        }
    }

    // Método para registrar lance em leilão eletrônico
    private boolean registrarLanceEletronico(LeilaoEletronico leilao, Cliente cliente, double valor) {

        if (cliente.getLancesDisponiveis() <= 0) {
            return false;
        }

        double ultimoLance = leilao.getLances().isEmpty() ? leilao.getValorMinimo() : leilao.getLances().getLast().getValor();

        // Verifica se o lance é válido (maior que o último lance e múltiplo do incremento)
        if (valor > ultimoLance && (valor - ultimoLance) % leilao.getMultiploLance() == 0) {
            Lance lance = new Lance(cliente, leilao, valor, LocalDateTime.now());
            leilao.getLances().add(lance); // Adiciona o lance ao leilão
            cliente.setLancesDisponiveis(cliente.getLancesDisponiveis() - 1); // Decrementa os lances do cliente
            return true;
        } else {
            return false;
        }
    }

    // Método para registrar lance em leilão carta fechada
    private boolean registrarLanceCartaFechada(LeilaoCartaFechada leilao, Cliente cliente, double valor) {
        // Verifica se o cliente já fez um lance neste leilão
        boolean clienteJaFezLance = leilao.getLances().stream()
                .anyMatch(lance -> lance.getCliente().equals(cliente));

        if (clienteJaFezLance) {
            return false;
        }

        // Verifica se o lance é maior ou igual ao valor mínimo
        if (valor >= leilao.getValorMinimo()) {
            Lance lance = new Lance(cliente, leilao, valor, LocalDateTime.now());
            leilao.getLances().add(lance); // Adiciona o lance ao leilão
            return true;
        } else {
            return false;
        }
    }

    // Método para registrar lance em leilão venda direta
    private boolean registrarLanceVendaDireta(LeilaoVendaDireta leilao, Cliente cliente, double valor) {
        // Verifica se o lance é maior ou igual ao valor mínimo
        if (valor >= leilao.getValorMinimo()) {
            Lance lance = new Lance(cliente, leilao, valor, LocalDateTime.now());
            leilao.getLances().add(lance);
            return true;
        } else {
            return false;
        }
    }


    // Método para listar leilões em que um cliente está inscrito
    public List<Leilao> listarLeiloesPorCliente(Cliente cliente) {
        return leiloes.stream()
                .filter(leilao -> leilao.getClientesInscritos().stream()
                        .anyMatch(c -> c.getId() == cliente.getId()))
                .collect(Collectors.toList());
    }

    // Método para listar leilões a terminar (data fim próxima)
    public List<Leilao> listarLeiloesATerminar() {
        LocalDate hoje = LocalDate.now();
        return leiloes.stream()
                .filter(leilao -> leilao.getDataFim() != null && leilao.getDataFim().isAfter(hoje) && leilao.getDataFim().isBefore(hoje.plusDays(7)) && leilao.isAtivo() && !leilao.isFechado() || leilao.getDataFim().isEqual(hoje)) // Leilões que terminam em até 7 dias
                .collect(Collectors.toList());
    }

    // Método para listar leilões ativos
    public List<Leilao> listarLeiloesAtivos() {
        return leiloes.stream()
                .filter(Leilao::isAtivo)
                .collect(Collectors.toList());
    }

    public boolean inscreverClienteEmLeilao(Leilao leilao, Cliente cliente) {
        if (leilao != null && cliente != null) {
            // Verifica se o cliente já está inscrito
            boolean jaInscrito = leilao.getClientesInscritos().stream()
                    .anyMatch(c -> c.getId() == cliente.getId());

            if (!jaInscrito) {
                leilao.getClientesInscritos().add(cliente);
                return true;
            }
        }
        return false;
    }


    public List<Lance> obterTodosLances() {
        List<Lance> todosLances = new ArrayList<>();
        for (Leilao leilao : leiloes) {
            todosLances.addAll(leilao.getLances());
        }
        return todosLances;
    }

}