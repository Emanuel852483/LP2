package Model;

import java.time.LocalDate;

public class LeilaoVendaDireta extends Leilao {

    // Construtor
    public LeilaoVendaDireta(String nomeProduto, String descricao, LocalDate dataInicio, LocalDate dataFim, double valorMinimo, boolean isAtivo, boolean isFechado) {
        super(nomeProduto, descricao, "Venda Direta", dataInicio, dataFim, valorMinimo, isAtivo, isFechado);
    }
}