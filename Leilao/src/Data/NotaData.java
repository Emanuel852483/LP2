package Data;

import Model.Nota;
import java.io.*;
import java.util.*;

public class NotaData {
    private static final String FILE_PATH = "data/Nota.csv";

    // Carrega notas do ficheiro CSV
    public static List<Nota> carregarNotas() {
        List<Nota> notas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String linha;
            br.readLine(); // Ignora o cabeçalho
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");
                int id = Integer.parseInt(dados[0]);
                String valor = dados[1];
                Nota nota = new Nota(id,valor);
                nota.setId(id);
                notas.add(nota);
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar notas: " + e.getMessage());
        }
        return notas;
    }

    // Salva notas no ficheiro CSV
    public static void salvarNotas(List<Nota> notas) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            bw.write("id;valor");
            bw.newLine();

            for (Nota nota : notas) {
                String linha = String.format("%d;%s", nota.getId(), nota.getValor());
                bw.write(linha);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar notas: " + e.getMessage());
        }
    }
}