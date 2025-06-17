package Controller;

import Model.Cliente;
import Data.ClienteData;  // Import necessário para salvar os dados
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ClienteController {
    private List<Cliente> clientes;

    // Construtor
    public ClienteController() {
        this.clientes = new ArrayList<>();
    }

    // Método para definir a lista de clientes
    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    // Método para criar um cliente — adiciona LocalDate.now() como último login
    public Cliente criarCliente(String nome, String morada, LocalDate dataNascimento, String email, String password, int lancesDisponiveis, boolean isAdmin, double saldo) {
        return new Cliente(nome, morada, dataNascimento, email, password, lancesDisponiveis, isAdmin, saldo, LocalDate.now());
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

    // Método para autenticar um cliente (login)
    public Cliente autenticarCliente(String email, String password) {
        Cliente cliente = buscarClientePorEmail(email);
        if (cliente != null && verificarPassword(cliente, password)) {
            // Atualiza a data do último login para hoje
            cliente.setUltimoLogin(LocalDate.now());

            // Salva a lista atualizada de clientes no ficheiro CSV
            ClienteData.salvarClientes(clientes);

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

    public boolean tornarClienteAdminPorId(int id) {
        Cliente cliente = buscarClientePorId(id);
        if (cliente != null) {
            cliente.setAdmin(true);
            return true;
        }
        return false;
    }

    // Método para listar todos os clientes
    public List<Cliente> listarClientes() {
        return clientes;
    }

    // Método para comprar lances
    public boolean comprarLances(Cliente cliente, int quantidade) {
        if (quantidade > 0) {
            cliente.setLancesDisponiveis(cliente.getLancesDisponiveis() + quantidade);
            return true;
        } else {
            return false;
        }
    }
}
