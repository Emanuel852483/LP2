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
    public static List<Lance> carregarLances(List<Cliente> clientes, List<Leilao> leiloes) {
        List<Lance> lances = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String linha;
            br.readLine(); // Ignora o cabeçalho
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length < 5) {
                    continue;
                }
                int id = dados[0].trim().isEmpty() ? 0 : Integer.parseInt(dados[0].trim());
                int clienteId = dados[1].trim().isEmpty() ? 0 : Integer.parseInt(dados[1].trim());
                int leilaoId = dados[2].trim().isEmpty() ? 0 : Integer.parseInt(dados[2].trim());
                double valor = dados[3].trim().isEmpty() ? 0.0 : Double.parseDouble(dados[3].trim());
                LocalDateTime dataHora = LocalDateTime.parse(dados[4], DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

                Cliente cliente = null;
                for (Cliente c : clientes) {
                    if (c.getId() == clienteId) {
                        cliente = c;
                        break;
                    }
                }

                Leilao leilao = null;
                for (Leilao l : leiloes) {
                    if (l.getId() == leilaoId) {
                        leilao = l;
                        break;
                    }
                }

                if (cliente != null && leilao != null) {
                    Lance lance = new Lance(cliente, leilao, valor, dataHora);
                    lance.setId(id);
                    lances.add(lance);
                }
            }
            Lance.initializeId(lances);
        } catch (IOException e) {
            System.out.println("Erro ao ler o ficheiro Lances.csv: " + e.getMessage());
        }
        return lances;
    }

    // Método para salvar lances no ficheiro CSV
    public static synchronized void salvarLances(List<Lance> lances) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            // Cabeçalho do arquivo CSV
            bw.write("id;clienteId;leilaoId;valor;dataHora");
            bw.newLine();

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