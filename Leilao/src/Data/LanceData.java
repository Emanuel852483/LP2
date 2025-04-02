package Data;

import Model.Lance;
import Model.Cliente;
import Model.Leilao;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class LanceData {
    private static final String FILE_PATH = "data/Lance.csv";

    // Método para carregar lances do ficheiro CSV
    public List<Lance> carregarLances(List<Cliente> clientes, List<Leilao> leiloes) {
        List<Lance> lances = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String linha;
            br.readLine(); // Ignora o cabeçalho
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");
                int id = Integer.parseInt(dados[0]);
                int clienteId = Integer.parseInt(dados[1]);
                int leilaoId = Integer.parseInt(dados[2]);
                double valor = Double.parseDouble(dados[3]);
                LocalDateTime dataHora = LocalDateTime.parse(dados[4], DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

                // Encontrar o Cliente e o Leilão correspondentes
                Cliente cliente = clientes.stream().filter(c -> c.getId() == clienteId).findFirst().orElse(null);
                Leilao leilao = leiloes.stream().filter(l -> l.getId() == leilaoId).findFirst().orElse(null);

                if (cliente != null && leilao != null) {
                    Lance lance = new Lance(cliente, leilao, valor, dataHora);
                    lance.setId(id); // Define o ID manualmente
                    lances.add(lance);
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o ficheiro Lances.csv: " + e.getMessage());
        }
        return lances;
    }

    // Método para salvar lances no ficheiro CSV
    public void salvarLances(List<Lance> lances) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            // Cabeçalho do arquivo CSV
            bw.write("id;clienteId;leilaoId;valor;dataHora");
            bw.newLine();

            // Escreve cada lance no arquivo
            for (Lance lance : lances) {
                String linha = String.format(Locale.US,
                        "%d;%d;%d;%.2f;%s",
                        lance.getId(),
                        lance.getCliente().getId(),
                        lance.getLeilao().getId(),
                        lance.getValor(),
                        lance.getDataHora().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
                bw.write(linha);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar o ficheiro Lances.csv: " + e.getMessage());
        }
    }

}