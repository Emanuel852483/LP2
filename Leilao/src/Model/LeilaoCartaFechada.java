package Model;

import java.time.LocalDate;

public class LeilaoCartaFechada extends Leilao {

    // Construtor
    public LeilaoCartaFechada(String nomeProduto, String descricao, LocalDate dataInicio, LocalDate dataFim, double valorMinimo, boolean isAtivo, boolean isFechado) {
        super(nomeProduto, descricao, "Carta Fechada", dataInicio, dataFim, valorMinimo, isAtivo, isFechado);
    }
}
