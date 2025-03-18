package Model;

import java.time.LocalDate;

public class LeilaoEletronico extends Leilao {
    private double multiploLance;

    // Construtor
    public LeilaoEletronico(String nomeProduto, String descricao, LocalDate dataInicio, LocalDate dataFim, double valorMinimo, double valorMaximo, double multiploLance) {
        super(nomeProduto, descricao, "Eletrônico", dataInicio, dataFim, valorMinimo, valorMaximo);
        this.multiploLance = multiploLance;
    }

    // Getter e Setter
    public double getMultiploLance() {
        return multiploLance;
    }

    public void setMultiploLance(double multiploLance) {
        this.multiploLance = multiploLance;
    }
}