package Model;

import java.time.LocalDateTime;

public class Agente {
    private int id;
    private Cliente cliente;
    private Leilao leilao;
    private double valorMaximo;
    private double incremento;
    private LocalDateTime dataCriacao;
    private boolean ativo;

    public Agente(Cliente cliente, Leilao leilao, double valorMaximo, double incremento) {
        this.cliente = cliente;
        this.leilao = leilao;
        this.valorMaximo = valorMaximo;
        this.incremento = incremento;
        this.dataCriacao = LocalDateTime.now();
        this.ativo = true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public double getValorMaximo() {
        return valorMaximo;
    }

    public void setValorMaximo(double valorMaximo) {
        this.valorMaximo = valorMaximo;
    }

    public double getIncremento() {
        return incremento;
    }

    public void setIncremento(double incremento) {
        this.incremento = incremento;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
