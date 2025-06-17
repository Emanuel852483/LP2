package Model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Lance {
    private static final AtomicInteger proximoId = new AtomicInteger(1);

    private int id;
    private Cliente cliente;
    private Leilao leilao;
    private double valor;
    private LocalDateTime dataHora;

    // Construtor
    public Lance(Cliente cliente, Leilao leilao, double valor, LocalDateTime dataHora) {
        this.id = proximoId.getAndIncrement();
        this.cliente = cliente;
        this.leilao = leilao;
        this.valor = valor;
        this.dataHora = dataHora;
    }

    // Getters e Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static void initializeId(List<Lance> lancesExistentes) {
        int maxId = lancesExistentes.stream()
                .mapToInt(Lance::getId)
                .max()
                .orElse(0);
        proximoId.set(maxId + 1);
    }

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
                "cliente=" + cliente.getId() +
                ", leilao=" + leilao.getId() +
                ", valor=" + valor +
                ", dataHora=" + dataHora +
                '}';
    }
}