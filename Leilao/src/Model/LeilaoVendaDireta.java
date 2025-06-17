package Model;


import java.time.LocalDateTime;

public class LeilaoVendaDireta extends Leilao {

    // Construtor
    public LeilaoVendaDireta(String nomeProduto, String descricao, LocalDateTime dataInicio, LocalDateTime dataFim, double valorMinimo, boolean isAtivo, boolean isFechado) {
        super(nomeProduto, descricao, "Venda Direta", dataInicio, dataFim, valorMinimo, isAtivo, isFechado);
    }
}