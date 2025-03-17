package Model;

import java.time.LocalDate;

public class LeilaoVendaDireta extends Leilao {

    // Construtor
    public LeilaoVendaDireta(String nomeProduto, String descricao, LocalDate dataInicio, LocalDate dataFim, double valorMinimo) {
        super(nomeProduto, descricao, "Venda Direta", dataInicio, dataFim, valorMinimo, 0); // Valor máximo não se aplica
    }
}