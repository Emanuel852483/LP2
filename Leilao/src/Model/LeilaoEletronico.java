package Model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class LeilaoEletronico extends Leilao {
    private double multiploLance;

    // Construtor
    public LeilaoEletronico(String nomeProduto, String descricao, LocalDateTime dataInicio, LocalDateTime dataFim, double valorMinimo, double multiploLance, boolean isAtivo, boolean isFechado) {
        super(nomeProduto, descricao, "Eletrônico", dataInicio, dataFim, valorMinimo, isAtivo, isFechado);
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