package Data;

import Model.Leilao;
import Model.LeilaoCartaFechada;
import Model.LeilaoEletronico;
import Model.LeilaoVendaDireta;
import java.util.Locale;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LeilaoData {
    private static final String FILE_PATH = "data/Leilao.csv";

    // Método para carregar leilões do ficheiro CSV
    public List<Leilao> carregarLeiloes() {
        List<Leilao> leiloes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String linha;
            br.readLine(); // Ignora o cabeçalho
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";"); // Usa ";" como separador
                int id = Integer.parseInt(dados[0]);
                String nomeProduto = dados[1];
                String descricao = dados[2];
                String tipoLeilao = dados[3];
                LocalDate dataInicio = LocalDate.parse(dados[4], DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                LocalDate dataFim = LocalDate.parse(dados[5], DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                double valorMinimo = Double.parseDouble(dados[6].replace(",", "."));
                double valorMaximo = dados[7].isEmpty() ? 0.0 : Double.parseDouble(dados[7].replace(",", "."));
                double multiploLance = dados[8].isEmpty() ? 0.0 : Double.parseDouble(dados[8].replace(",", "."));

                // Cria o leilão com base no tipo
                Leilao leilao;
                switch (tipoLeilao) {
                    case "Eletrônico":
                        leilao = new LeilaoEletronico(nomeProduto, descricao, dataInicio, dataFim, valorMinimo, valorMaximo, multiploLance);
                        break;
                    case "Carta Fechada":
                        leilao = new LeilaoCartaFechada(nomeProduto, descricao, dataInicio, dataFim, valorMinimo);
                        break;
                    case "Venda Direta":
                        leilao = new LeilaoVendaDireta(nomeProduto, descricao, dataInicio, dataFim, valorMinimo);
                        break;
                    default:
                        throw new IllegalArgumentException("Tipo de leilão inválido: " + tipoLeilao);
                }

                // Define o ID manualmente (já que ele foi lido do ficheiro)
                leilao.setId(id);

                leiloes.add(leilao);
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o ficheiro Leiloes.csv: " + e.getMessage());
        }
        return leiloes;
    }

    // Método para salvar leilões no ficheiro CSV
    public void salvarLeiloes(List<Leilao> leiloes) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            // Cabeçalho do arquivo CSV
            bw.write("id;nomeProduto;descricao;tipoLeilao;dataInicio;dataFim;valorMinimo;valorMaximo;multiploLance");
            bw.newLine();

            // Escreve cada leilão no arquivo
            for (Leilao leilao : leiloes) {
                String linha = String.format(Locale.US, // Usa Locale.US para garantir pontos como separadores
                        "%d;%s;%s;%s;%s;%s;%.2f;%.2f;%.2f",
                        leilao.getId(),
                        leilao.getNomeProduto(),
                        leilao.getDescricao(),
                        leilao.getTipoLeilao(),
                        leilao.getDataInicio().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                        leilao.getDataFim().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                        leilao.getValorMinimo(),
                        leilao.getValorMaximo(),
                        leilao instanceof LeilaoEletronico ? ((LeilaoEletronico) leilao).getMultiploLance() : 0);
                bw.write(linha);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar o ficheiro Leiloes.csv: " + e.getMessage());
        }
    }
}
