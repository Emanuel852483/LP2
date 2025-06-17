package Model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Leilao {
    private static int proximoId = 1;

    private int id;
    private String nomeProduto;
    private String descricao;
    private String tipoLeilao;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private double valorMinimo;
    private  boolean isAtivo;
    private boolean isFechado;
    private List<Lance> lances;
    private List<Cliente> clientesInscritos;
    private Cliente vencedor;
    private List<AvaliacaoLeilao>avaliacoesdosclientes;

    // Construtor
    public Leilao(String nomeProduto, String descricao, String tipoLeilao, LocalDateTime dataInicio, LocalDateTime dataFim, double valorMinimo, boolean isAtivo, boolean isFechado) {
        this.id = proximoId++;
        this.nomeProduto = nomeProduto;
        this.descricao = descricao;
        this.tipoLeilao = tipoLeilao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.valorMinimo = valorMinimo;
        this.isAtivo = isAtivo;
        this.isFechado = isFechado;
        this.lances = new ArrayList<>();
        this.clientesInscritos = new ArrayList<>();
        this.vencedor = null;
        this.avaliacoesdosclientes = new ArrayList<>();
    }

    // Getters e Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public static void setProximoId(int proximoId) {
        Leilao.proximoId = proximoId;
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

    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDateTime dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDateTime getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDateTime dataFim) {
        this.dataFim = dataFim;
    }

    public double getValorMinimo() {
        return valorMinimo;
    }

    public void setValorMinimo(double valorMinimo) {
        this.valorMinimo = valorMinimo;
    }


    public boolean isAtivo() {
        return isAtivo;
    }

    public void setAtivo(boolean ativo) {
        isAtivo = ativo;
    }

    public boolean isFechado() {
        return isFechado;
    }

    public void setFechado(boolean fechado) {
        isFechado = fechado;
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

    public Cliente getVencedor() {
        return vencedor;
    }

    public void setVencedor(Cliente vencedor) {
        this.vencedor = vencedor;
    }

    public List<AvaliacaoLeilao> getAvaliacoesdosclientes() {
        return avaliacoesdosclientes;
    }

    public void setAvaliacoesdosclientes(List<AvaliacaoLeilao> avaliacoesdosclientes) {
        this.avaliacoesdosclientes = avaliacoesdosclientes;
    }
}