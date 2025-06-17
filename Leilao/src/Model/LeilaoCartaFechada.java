package Model;


import java.time.LocalDateTime;

public class LeilaoCartaFechada extends Leilao {

    // Construtor
    public LeilaoCartaFechada(String nomeProduto, String descricao, LocalDateTime dataInicio, LocalDateTime dataFim, double valorMinimo, boolean isAtivo, boolean isFechado) {
        super(nomeProduto, descricao, "Carta Fechada", dataInicio, dataFim, valorMinimo, isAtivo, isFechado);
    }
}
