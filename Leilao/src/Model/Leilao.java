package Model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Leilao {
    private static int proximoId = 1;

    private int id;
    private String nomeProduto;
    private String descricao;
    private String tipoLeilao;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private double valorMinimo;
    private double valorMaximo;
    private List<Lance> lances;
    private List<Cliente> clientesInscritos;

    // Construtor
    public Leilao(String nomeProduto, String descricao, String tipoLeilao, LocalDate dataInicio, LocalDate dataFim, double valorMinimo, double valorMaximo) {
        this.id = proximoId++;
        this.nomeProduto = nomeProduto;
        this.descricao = descricao;
        this.tipoLeilao = tipoLeilao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.valorMinimo = valorMinimo;
        this.valorMaximo = valorMaximo;
        this.lances = new ArrayList<>();
        this.clientesInscritos = new ArrayList<>();
    }

    // Getters e Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipoLeilao() {
        return tipoLeilao;
    }

    public void setTipoLeilao(String tipoLeilao) {
        this.tipoLeilao = tipoLeilao;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public double getValorMinimo() {
        return valorMinimo;
    }

    public void setValorMinimo(double valorMinimo) {
        this.valorMinimo = valorMinimo;
    }

    public double getValorMaximo() {
        return valorMaximo;
    }

    public void setValorMaximo(double valorMaximo) {
        this.valorMaximo = valorMaximo;
    }

    public List<Lance> getLances() {
        return lances;
    }

    public void setLances(List<Lance> lances) {
        this.lances = lances;
    }

    public List<Cliente> getClientesInscritos() {
        return clientesInscritos;
    }

    public void setClientesInscritos(List<Cliente> clientesInscritos) {
        this.clientesInscritos = clientesInscritos;
    }
}
