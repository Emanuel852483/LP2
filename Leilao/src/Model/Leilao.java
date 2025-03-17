package Model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Leilao {
    private String nomeProduto;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private double valorMinimo;
    private List<Lance> lances;

    public Leilao(String nomeProduto, LocalDateTime dataInicio, LocalDateTime dataFim, double valorMinimo) {
        this.nomeProduto = nomeProduto;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.valorMinimo = valorMinimo;
        this.lances = new ArrayList<>();
    }

    public String getNomeProduto() {
        return nomeProduto;
    }
    public LocalDateTime getDataInicio() {
        return dataInicio;
    }
    public LocalDateTime getDataFim() {
        return dataFim;
    }
    public List<Lance> getLances() {
        return lances;
    }

    public void adicionarLance(Lance lance) {
        lances.add(lance);
    }
}
