package Controller;

import Model.*;

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

    public void setLeiloes(List<Leilao> leiloes) {
        this.leiloes = leiloes;
    }

    // Método para adicionar um leilão
    public void adicionarLeilao(Leilao leilao) {
        leiloes.add(leilao);
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
    public List<Leilao> listarLeiloes() {
        return leiloes;
    }

    // Método para criar um leilão eletrônico
    public LeilaoEletronico criarLeilaoEletronico(String nomeProduto, String descricao, LocalDate dataInicio, LocalDate dataFim, double valorMinimo, double valorMaximo, double multiploLance) {
        return new LeilaoEletronico(nomeProduto, descricao, dataInicio, dataFim, valorMinimo, valorMaximo, multiploLance);
    }

    // Método para criar um leilão carta fechada
    public LeilaoCartaFechada criarLeilaoCartaFechada(String nomeProduto, String descricao, LocalDate dataInicio, LocalDate dataFim, double valorMinimo) {
        return new LeilaoCartaFechada(nomeProduto, descricao, dataInicio, dataFim, valorMinimo);
    }

    // Método para criar um leilão venda direta
    public LeilaoVendaDireta criarLeilaoVendaDireta(String nomeProduto, String descricao, LocalDate dataInicio, LocalDate dataFim, double valorMinimo) {
        return new LeilaoVendaDireta(nomeProduto, descricao, dataInicio, dataFim, valorMinimo);
    }

    public boolean registrarLance(Leilao leilao, Cliente cliente, double valor) {
        if (leilao == null || cliente == null) {
            return false;
        }

        // Verifica o tipo de leilão e aplica as regras específicas
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
                .filter(leilao -> leilao.getDataFim() != null && leilao.getDataFim().isAfter(hoje) && leilao.getDataFim().isBefore(hoje.plusDays(7))) // Leilões que terminam em até 7 dias
                .collect(Collectors.toList());
    }

    // Método para listar leilões ativos (data início <= hoje <= data fim)
    public List<Leilao> listarLeiloesAtivos() {
        LocalDate hoje = LocalDate.now();
        return leiloes.stream()
                .filter(leilao -> (leilao.getDataInicio() == null || leilao.getDataInicio().isBefore(hoje) || leilao.getDataInicio().isEqual(hoje))
                        && (leilao.getDataFim() == null || leilao.getDataFim().isAfter(hoje) || leilao.getDataFim().isEqual(hoje)))
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


}