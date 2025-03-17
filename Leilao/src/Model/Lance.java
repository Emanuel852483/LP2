package Model;

import java.time.LocalDateTime;

public class Lance {
    private Cliente cliente;
    private double valor;
    private LocalDateTime dataHora;

    public Lance(Cliente cliente, double valor, LocalDateTime dataHora) {
        this.cliente = cliente;
        this.valor = valor;
        this.dataHora = dataHora;
    }

    public Cliente getCliente() {
        return cliente;
    }
    public double getValor() {
        return valor;
    }
    public LocalDateTime getDataHora() {
        return dataHora;
    }
}
