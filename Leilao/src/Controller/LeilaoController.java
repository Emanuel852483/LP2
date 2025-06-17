package Controller;

import Data.AvaliacaoLeilaoData;
import Data.ClienteData;
import Data.LanceData;
import Model.*;
import Data.LeilaoData;
import Data.NotaData;
import Controller.NotificacaoController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;




public class LeilaoController {

    private static List<Leilao> leiloes;
    private final ClienteController clienteController;
    private AgenteController agenteController;
    private final List<Lance> lances;


    public LeilaoController(ClienteController clienteController) {
        this.clienteController = clienteController;


        leiloes = new LeilaoData().carregarLeiloes(ClienteController.listarClientes(), new ArrayList<>(), new ArrayList<>());

        List<Nota> notas = NotaData.carregarNotas();
        List<AvaliacaoLeilao> avaliacoes = AvaliacaoLeilaoData.carregarAvaliacoes(ClienteController.listarClientes(), leiloes, notas);

        this.lances = LanceData.carregarLances(ClienteController.listarClientes(), leiloes);

        leiloes = new LeilaoData().carregarLeiloes(ClienteController.listarClientes(), this.lances, avaliacoes);

    }

    public void setAgenteController(AgenteController agenteController) {
        this.agenteController = agenteController;
    }


    public  List<Leilao> getLeiloes() {
        return leiloes;
    }

    public List<Lance> getLances() {
        return this.lances;
    }

    public void verificarStatusLeiloes() {
        verificarStatusLeiloes(leiloes, this.clienteController);
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
    public static List<Leilao> listarLeiloes()
    {
        return leiloes;
    }

    // Método para criar um leilão eletrônico
    public LeilaoEletronico criarLeilaoEletronico(String nomeProduto, String descricao, LocalDateTime dataInicio, LocalDateTime dataFim, double valorMinimo, double multiploLance, boolean isAtivo, boolean isFechado) {
        return new LeilaoEletronico(nomeProduto, descricao, dataInicio, dataFim, valorMinimo, multiploLance, isAtivo, isFechado);
    }

    // Método para criar um leilão carta fechada
    public LeilaoCartaFechada criarLeilaoCartaFechada(String nomeProduto, String descricao, LocalDateTime dataInicio, LocalDateTime dataFim, double valorMinimo, boolean isAtivo, boolean isFechado) {
        return new LeilaoCartaFechada(nomeProduto, descricao, dataInicio, dataFim, valorMinimo, isAtivo, isFechado);
    }

    // Método para criar um leilão venda direta
    public LeilaoVendaDireta criarLeilaoVendaDireta(String nomeProduto, String descricao, LocalDateTime dataInicio, LocalDateTime dataFim, double valorMinimo, boolean isAtivo, boolean isFechado) {
        return new LeilaoVendaDireta(nomeProduto, descricao, dataInicio, dataFim, valorMinimo, isAtivo, isFechado);


    }

    public void verificarStatusLeiloes(List<Leilao> leiloes, ClienteController clienteController) {
        LocalDateTime hoje = LocalDateTime.now();
        boolean modificado = false;

        for (Leilao leilao : leiloes) {
            boolean dentroDoPeriodo = !hoje.isBefore(leilao.getDataInicio()) &&
                    !hoje.isAfter(leilao.getDataFim());
            boolean periodoEncerrado = hoje.isAfter(leilao.getDataFim());

            if (leilao instanceof LeilaoVendaDireta lvd) {

                if (!lvd.getLances().isEmpty()) {
                    if (!lvd.isFechado()) {
                        lvd.setFechado(true);
                        lvd.setAtivo(false);
                        modificado = true;
                    }
                    continue;
                }

                if (lvd.isAtivo() != dentroDoPeriodo || lvd.isFechado() == dentroDoPeriodo) {
                    lvd.setAtivo(dentroDoPeriodo);
                    lvd.setFechado(!dentroDoPeriodo);
                    modificado = true;
                }
                continue;
            }

            if (dentroDoPeriodo && leilao.isFechado()) {
                if (leilao.getLances().isEmpty()) {
                    leilao.setFechado(false);
                    leilao.setAtivo(true);
                    leilao.setVencedor(null);
                    modificado = true;
                    continue;
                }
            }

            if (periodoEncerrado && !leilao.isFechado()) {
                List<Lance> lancesBackup = new ArrayList<>(leilao.getLances());

                leilao.setFechado(true);
                leilao.setAtivo(false);
                modificado = true;

                Cliente vencedor = definirVencedor(leilao);

                if (vencedor != null) {
                    NotificacaoController.enviarEmailVencedorLeilao(
                            vencedor.getEmail(),
                            vencedor.getNome(),
                            leilao.getNomeProduto(),
                            leilao.getLances().isEmpty() ? 0 : leilao.getLances().getLast().getValor()
                    );
                }

                if (leilao instanceof LeilaoCartaFechada) {
                    devolverSaldoPerdedores((LeilaoCartaFechada) leilao, clienteController);
                }

                leilao.setLances(lancesBackup);
            }
            else if (leilao.isAtivo() != dentroDoPeriodo) {
                leilao.setAtivo(dentroDoPeriodo);
                modificado = true;
            }
        }

        if (modificado) {
            LeilaoData.salvarLeiloes(leiloes);
        }
    }

    private Cliente definirVencedor(Leilao leilao) {
        if (leilao.getLances() == null || leilao.getLances().isEmpty()) {
            return null;
        }

        Cliente vencedor = null;

        if (leilao instanceof LeilaoEletronico) {
            Lance ultimoLance = leilao.getLances().getLast();
            vencedor = ultimoLance.getCliente();
        }
        else if (leilao instanceof LeilaoCartaFechada) {
            Lance maiorLance = null;
            for (Lance lance : leilao.getLances()) {
                if (maiorLance == null || lance.getValor() > maiorLance.getValor()) {
                    maiorLance = lance;
                }
            }
            vencedor = maiorLance != null ? maiorLance.getCliente() : null;
        }

        leilao.setVencedor(vencedor);
        return vencedor;
    }

    private void devolverSaldoPerdedores(LeilaoCartaFechada leilao, ClienteController clienteController) {
        if (leilao.getVencedor() == null || leilao.getLances() == null) {
            return;
        }

        List<Cliente> todosClientes = clienteController.listarClientes();
        boolean modificado = false;


        for (Lance lance : leilao.getLances()) {
            if (!lance.getCliente().equals(leilao.getVencedor())) {
                for (Cliente cliente : todosClientes) {
                    if (cliente.equals(lance.getCliente())) {
                        cliente.setSaldo(cliente.getSaldo() + lance.getValor());
                        modificado = true;
                        break;
                    }
                }
            }
        }

        if (modificado) {
            ClienteData.salvarClientes(todosClientes);
        }
    }




    public boolean registrarLance(Leilao leilao, Cliente cliente, double valor) {
        if (leilao == null || cliente == null || cliente.getSaldo() < valor) {
            return false;
        }

        boolean sucesso = false;

        if (leilao instanceof LeilaoEletronico) {
            sucesso = registrarLanceEletronico((LeilaoEletronico) leilao, cliente, valor);
        } else if (leilao instanceof LeilaoCartaFechada) {
            sucesso = registrarLanceCartaFechada((LeilaoCartaFechada) leilao, cliente, valor);
        } else if (leilao instanceof LeilaoVendaDireta) {
            sucesso = registrarLanceVendaDireta((LeilaoVendaDireta) leilao, cliente, valor);
        }

        if (sucesso) {
            if (leilao instanceof LeilaoEletronico) {
                Lance novoLance = leilao.getLances().getLast();
                new Thread(() -> agenteController.processarNovoLance(novoLance)).start();
            }

            cliente.setSaldo(cliente.getSaldo() - valor);
            LanceData.salvarLances(this.lances);
            LeilaoData.salvarLeiloes(leiloes);
            ClienteData.salvarClientes(ClienteController.listarClientes());
        }

        return sucesso;
    }

    private boolean registrarLanceEletronico(LeilaoEletronico leilao, Cliente cliente, double valor) {
        List<Lance> lances = leilao.getLances();
        Lance novoLance = null;

        if (lances.isEmpty()) {
            if (valor >= leilao.getValorMinimo()) {
                novoLance = new Lance(cliente, leilao, valor, LocalDateTime.now());
                leilao.getLances().add(novoLance);
                this.lances.add(novoLance);
            }
        } else {
            double ultimoLance = lances.getLast().getValor();
            if (valor > ultimoLance && (valor - ultimoLance) % leilao.getMultiploLance() == 0) {
                Cliente clienteAnterior = lances.getLast().getCliente();
                clienteAnterior.setSaldo(clienteAnterior.getSaldo() + ultimoLance);

                novoLance = new Lance(cliente, leilao, valor, LocalDateTime.now());
                leilao.getLances().add(novoLance);
                this.lances.add(novoLance);
            }
        }

        return novoLance != null;
    }

    private boolean registrarLanceCartaFechada(LeilaoCartaFechada leilao, Cliente cliente, double valor) {
        boolean clienteJaFezLance = leilao.getLances().stream()
                .anyMatch(lance -> lance.getCliente().equals(cliente));

        if (clienteJaFezLance) {
            return false;
        }

        if (valor >= leilao.getValorMinimo()) {
            Lance lance = new Lance(cliente, leilao, valor, LocalDateTime.now());
            leilao.getLances().add(lance);
            this.lances.add(lance);
            return true;
        }
        return false;
    }

    private boolean registrarLanceVendaDireta(LeilaoVendaDireta leilao, Cliente cliente, double valor) {
        if (valor >= leilao.getValorMinimo()) {
            Lance lance = new Lance(cliente, leilao, valor, LocalDateTime.now());
            leilao.getLances().add(lance);
            this.lances.add(lance);

            leilao.setVencedor(cliente);
            leilao.getClientesInscritos().add(cliente);
            leilao.setAtivo(false);
            leilao.setFechado(true);

            NotificacaoController.enviarEmailVencedorLeilao(
                    cliente.getEmail(),
                    cliente.getNome(),
                    leilao.getNomeProduto(),
                    valor
            );

            return true;
        }
        return false;
    }



    public List<Leilao> listarLeiloesPorCliente(Cliente cliente) {
        return leiloes.stream()
                .filter(leilao -> leilao.getClientesInscritos().stream()
                        .anyMatch(c -> c.getId() == cliente.getId()))
                .collect(Collectors.toList());
    }


    public List<Leilao> listarLeiloesATerminar() {
        LocalDateTime hoje = LocalDateTime.now();
        return leiloes.stream()
                .filter(leilao -> leilao.getDataFim() != null && leilao.getDataFim().isAfter(hoje) && leilao.getDataFim().isBefore(hoje.plusDays(7)) && leilao.isAtivo() && !leilao.isFechado() || leilao.getDataFim().isEqual(hoje)) // Leilões que terminam em até 7 dias
                .collect(Collectors.toList());
    }


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




    public List<Lance> obterLancesPorLeilao(Leilao leilao) {
        if (leilao == null) return new ArrayList<>();
        return leilao.getLances();
    }





}