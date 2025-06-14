package Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import Model.Leilao;
import Model.Cliente;
import Model.Lance;
import Model.LeilaoCartaFechada;
import Model.LeilaoEletronico;
import Model.LeilaoVendaDireta;
import java.util.Locale;
import java.util.ArrayList;

public class Estatisticas {

    /**
     * Conta a quantidade de clientes registados no ficheiro CSV.
     */
    public static int contarClientesRegistados() {
        String caminho = "C:\\Users\\Lenovo\\IdeaProjects\\TESTELP2\\Leilao\\data\\Cliente.csv";
        int contador = 0;
        try (BufferedReader leitor = new BufferedReader(new FileReader(caminho))) {
            String linha;
            leitor.readLine(); // Ignora o cabeçalho
            while ((linha = leitor.readLine()) != null) {
                if (!linha.trim().isEmpty()) {
                    contador++;
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o ficheiro de clientes: " + e.getMessage());
        }
        return contador;
    }

    /**
     * Calcula a média de idades dos clientes registados com base na data de nascimento.
     */
    public static int calcularMediaIdades() {
        String caminho = "C:\\Users\\Lenovo\\IdeaProjects\\TESTELP2\\Leilao\\data\\Cliente.csv";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        long somaIdades = 0;
        int totalClientes = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(caminho.trim()))) {
            String linha = reader.readLine(); // Lê e ignora o cabeçalho
            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;

                String[] campos = linha.split(";");
                if (campos.length < 4) continue;

                String dataNascimentoStr = campos[3].trim(); // índice 3 = dataNascimento
                try {
                    LocalDate dataNascimento = LocalDate.parse(dataNascimentoStr, formatter);
                    long idade = ChronoUnit.YEARS.between(dataNascimento, LocalDate.now());
                    somaIdades += idade;
                    totalClientes++;
                } catch (DateTimeParseException e) {
                    System.err.println("Data inválida: " + dataNascimentoStr);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o ficheiro: " + e.getMessage());
            return 0;
        }

        if (totalClientes == 0) {
            System.err.println("Nenhum cliente válido encontrado.");
            return 0;
        }

        return (int) Math.round((double) somaIdades / totalClientes);
    }

    /**
     * Conta a quantidade de leilões que já foram terminados.
     * Um leilão é considerado terminado se não está ativo e está fechado.
     */
    public static int quantidadeLeiloesTerminados(List<Leilao> leiloes) {
        int count = 0;
        for (Leilao leilao : leiloes) {
            if (!leilao.isAtivo() && leilao.isFechado()) {
                count++;
            }
        }
        return count;
    }

    /**
     * Retorna o leilão que esteve mais tempo ativo (em dias).
     * Considera a diferença entre dataInicio e dataFim.
     */
    public static Leilao leilaoMaisTempoAtivo(List<Leilao> leiloes) {
        Leilao maisLongo = null;
        long maiorDuracao = 0;

        for (Leilao leilao : leiloes) {
            if (leilao.getDataInicio() != null && leilao.getDataFim() != null) {
                long diasDuracao = ChronoUnit.DAYS.between(leilao.getDataInicio(), leilao.getDataFim());
                if (diasDuracao > maiorDuracao) {
                    maiorDuracao = diasDuracao;
                    maisLongo = leilao;
                }
            }
        }

        return maisLongo;
    }
    public static void percentagemDominioMaisUsado() {
        String caminho = "C:\\Users\\Lenovo\\IdeaProjects\\TESTELP2\\Leilao\\data\\Cliente.csv";
        int totalClientes = 0;
        java.util.Map<String, Integer> contagemDominios = new java.util.HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(caminho))) {
            reader.readLine(); // Ignora cabeçalho
            String linha;

            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;

                String[] campos = linha.split(";");
                if (campos.length < 5) continue;

                String email = campos[4].trim(); // índice 4 = email
                if (!email.contains("@")) continue;

                String dominio = email.substring(email.indexOf('@') + 1);
                contagemDominios.put(dominio, contagemDominios.getOrDefault(dominio, 0) + 1);
                totalClientes++;
            }

        } catch (IOException e) {
            System.err.println("Erro ao ler o ficheiro: " + e.getMessage());
            return;
        }

        if (totalClientes == 0) {
            System.out.println("Nenhum cliente encontrado.");
            return;
        }

        String dominioMaisUsado = null;
        int maxContagem = 0;

        for (java.util.Map.Entry<String, Integer> entry : contagemDominios.entrySet()) {
            if (entry.getValue() > maxContagem) {
                maxContagem = entry.getValue();
                dominioMaisUsado = entry.getKey();
            }
        }

        double percentagem = (maxContagem * 100.0) / totalClientes;

        System.out.printf("Domínio mais usado: %s (%d clientes - %.2f%%)%n",
                dominioMaisUsado, maxContagem, percentagem);
    }

}



