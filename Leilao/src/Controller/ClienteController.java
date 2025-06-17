package Controller;

import Data.ClienteData;
import Model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ClienteController {
    private static List<Cliente> clientes;
    private final List<PedidoSaldo> pedidosSaldo;

    // Construtor
    public ClienteController() {
        clientes = ClienteData.carregarClientes();
        this.pedidosSaldo = new ArrayList<>();
    }

    // Método para definir a lista de clientes
    public void setClientes(List<Cliente> clientes) {
        ClienteController.clientes = clientes;
    }

    public List<Cliente> getClientes() {
        return clientes;
    }


    // Método para criar um cliente
    public Cliente criarCliente(String nome, String morada, LocalDate dataNascimento, String email, String password, int lancesDisponiveis, boolean isAdmin, double saldo, LocalDateTime ultimoLogin, String status) {
        return new Cliente(nome, morada, dataNascimento, email, password, lancesDisponiveis,isAdmin,saldo,ultimoLogin,status);
    }


    // Método para adicionar um cliente
    public boolean adicionarCliente(Cliente cliente) {
        return clientes.add(cliente);
    }

    // Método para remover um cliente
    public void removerCliente(Cliente cliente) {
        clientes.remove(cliente);
    }


    public Cliente buscarClientePorEmail(String email) {
        for (Cliente cliente : clientes) {
            if (cliente.getEmail().equalsIgnoreCase(email)) {
                return cliente;
            }
        }
        return null;
    }



    public boolean existeEmail(String email) {
        for (Cliente cliente : listarClientes()) {
            if (cliente.getEmail().equalsIgnoreCase(email.trim())) {
                return true;
            }
        }
        return false;
    }

    public boolean verificarPassword(Cliente cliente, String password) {
        if (cliente != null) {
            return cliente.getPassword().equals(password);
        }
        return false;
    }

    public void adicionarPedidoSaldo(PedidoSaldo pedido) {
        pedidosSaldo.add(pedido);
    }


    public void aprovarPedidoSaldo(PedidoSaldo pedido) {
        pedido.setAprovado(true);
        Cliente cliente = pedido.getCliente();
        cliente.setSaldo(cliente.getSaldo() + pedido.getQuantia());
    }

    public void verificarInatividadeClientes() {
        List<Cliente> clientes = ClienteData.carregarClientes();
        LocalDateTime hoje = LocalDateTime.now();

        for (Cliente cliente : clientes) {
            if (cliente.getUltimoLogin() != null &&
                    cliente.getUltimoLogin().plusMonths(3).isBefore(hoje)) {

                NotificacaoController.verificarEEnviarEmailInatividade(
                        cliente.getEmail(),
                        cliente.getNome(),
                        cliente.getUltimoLogin()
                );
            }
        }
    }

    // Método para autenticar um cliente (login)
    public Cliente autenticarCliente(String email, String password) {
        Cliente cliente = buscarClientePorEmail(email);
        if (cliente != null && verificarPassword(cliente, password)) {
            return cliente;
        }
        return null;
    }

    public Cliente buscarClientePorId(int id) {
        for (Cliente cliente : clientes) {
            if (cliente.getId() == id) {
                return cliente;
            }
        }
        return null;
    }

    public void aprovarCliente(Cliente cliente, boolean aprovar) {
        if (cliente == null) {
            return;
        }
        if (aprovar) {
            cliente.setStatus("APROVADO");
        } else {
            cliente.setStatus("REJEITADO");
        }

        listarClientesPendentes().remove(cliente);
    }

    public List<Cliente> listarClientesPendentes() {
        List<Cliente> pendentes = new ArrayList<>();
        for (Cliente c : clientes) {
            if ("PENDENTE".equals(c.getStatus())) {
                pendentes.add(c);
            }
        }
        return pendentes;
    }



    // Método para listar todos os clientes
    public static List<Cliente> listarClientes() {
        return clientes;
    }

    public AvaliacaoLeilao avaliarLeilao(Cliente cliente, Leilao leilao, Nota nota, String comentario) {
        if (leilao == null || cliente == null) {
            return null;
        }

        if (!leilao.isFechado()) {
            return null;
        }

        boolean inscrito = false;
        for (Cliente c : leilao.getClientesInscritos()) {
            if (c.getId() == cliente.getId()) {
                inscrito = true;
                break;
            }
        }
        if (!inscrito) {
            return null;
        }

        boolean jaAvaliou = false;
        for (AvaliacaoLeilao a : leilao.getAvaliacoesdosclientes()) {
            if (a.getCliente().getId() == cliente.getId()) {
                jaAvaliou = true;
                break;
            }
        }
        if (jaAvaliou) {
            return null;
        }

        AvaliacaoLeilao avaliacao = new AvaliacaoLeilao(cliente, leilao, nota, comentario);
        leilao.getAvaliacoesdosclientes().add(avaliacao);
        return avaliacao;
    }


}