package Model;

import java.time.LocalDate;

public class PedidoSaldo {
    private final Cliente cliente;
    private final double quantia;
    private final LocalDate dataPedido;
    private Boolean aprovado;

    public PedidoSaldo(Cliente cliente, double quantia, LocalDate dataPedido, Boolean aprovado) {
        this.cliente = cliente;
        this.quantia = quantia;
        this.dataPedido = dataPedido;
        this.aprovado = aprovado;
    }

    // Getters e Setters
    public Cliente getCliente() {
        return cliente;
    }

    public double getQuantia() {
        return quantia;
    }

    public LocalDate getDataPedido() {
        return dataPedido;
    }

    public Boolean isAprovado() {
        return aprovado;
    }

    public void setAprovado(Boolean aprovado) {
        this.aprovado = aprovado;
    }

    @Override
    public String toString() {
        return "PedidoSaldo{" +
                "cliente=" + cliente.getNome() +
                ", quantia=" + quantia +
                ", dataPedido=" + dataPedido +
                ", aprovado=" + aprovado +
                '}';
    }
}
