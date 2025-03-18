package Model;

import java.time.LocalDateTime;
import Model.Cliente;
import Model.Leilao;

public class Lance {
    private Cliente cliente;
    private Leilao leilao;
    private double valor;
    private LocalDateTime dataHora;

    // Construtor
    public Lance(Cliente cliente, Leilao leilao, double valor, LocalDateTime dataHora) {
        this.cliente = cliente;
        this.leilao = leilao;
        this.valor = valor;
        this.dataHora = dataHora;
    }

    // Getters e Setters
    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Leilao getLeilao() {
        return leilao;
    }

    public void setLeilao(Leilao leilao) {
        this.leilao = leilao;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    // Método toString para representação textual do lance
    @Override
    public String toString() {
        return "Lance{" +
                "cliente=" + cliente.getNome() +
                ", leilao=" + leilao.getNomeProduto() +
                ", valor=" + valor +
                ", dataHora=" + dataHora +
                '}';
    }
}
