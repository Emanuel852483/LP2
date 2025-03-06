package Controller;

import java.util.List;
import java.util.Arraylist;

public class ClienteController {
    private List<Cliente> clientes;

    public ClienteController() {
        this.clientes = new ArrayList<>();
    }

    //Create
    public void adicionarCliente(Cliente cliente) {
        clientes.add(cliente);
    }

    //Read
    public List<Cliente> listarClientes() {
        return clientes;
    }

    //Update
    public void atualizarClientes(int index, Cliente clientes) {
        if (index >= 0 && index < clientes.size()) {
            clientes.set(index, cliente);
        }
    }

    //Delete
    public void removerCliente(int index) {
        if (index >= 0 && index < clientes.size()) {
            clientes.remove(index);
        }
    }
}
