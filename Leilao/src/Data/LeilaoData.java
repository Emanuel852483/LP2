package Data;

import Model.Leilao;
import Model.Lance;
import Model.Cliente;
import Model.LeilaoCartaFechada;
import Model.LeilaoEletronico;
import Model.LeilaoVendaDireta;
import java.util.Locale;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LeilaoData {
    private static final String FILE_PATH = "data/Leilao.csv";

    // Método para carregar leilões do ficheiro CSV
    public List<Leilao> carregarLeiloes(List<Cliente> clientes, List<Lance> lances) {
        List<Leilao> leiloes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String linha;
            br.readLine(); // Ignora o cabeçalho
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";", -1); // O -1 mantem campos vazios

                // Verifica número mínimo de campos
                if (dados.length < 9) {
                    System.out.println("Linha ignorada (campos insuficientes): " + linha);
                    continue;
                }

                try {
                    int id = Integer.parseInt(dados[0]);
                    String nomeProduto = dados[1];
                    String descricao = dados[2];
                    String tipoLeilao = dados[3];
                    LocalDate dataInicio = LocalDate.parse(dados[4], DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                    LocalDate dataFim = LocalDate.parse(dados[5], DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                    double valorMinimo = Double.parseDouble(dados[6].replace(",", "."));


                    double valorMaximo = (dados.length > 7 && !dados[7].isEmpty()) ?
                            Double.parseDouble(dados[7].replace(",", ".")) : 0.0;
                    double multiploLance = (dados.length > 8 && !dados[8].isEmpty()) ?
                            Double.parseDouble(dados[8].replace(",", ".")) : 0.0;


                    List<Integer> lancesIds = (dados.length > 9) ? parseIdList(dados[9]) : new ArrayList<>();
                    List<Integer> clientesIds = (dados.length > 10) ? parseIdList(dados[10]) : new ArrayList<>();

                    // Cria o leilão
                    Leilao leilao = criarLeilaoPorTipo( nomeProduto, descricao, tipoLeilao,
                            dataInicio, dataFim, valorMinimo, valorMaximo, multiploLance);
                    leilao.setId(id);

                    // Associa lances e clientes
                    associarLances(leilao, lancesIds, lances);
                    associarClientes(leilao, clientesIds, clientes);

                    leiloes.add(leilao);
                } catch (Exception e) {
                    System.out.println("Erro ao processar linha: " + linha);
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o ficheiro Leiloes.csv: " + e.getMessage());
        }
        return leiloes;
    }

    private Leilao criarLeilaoPorTipo(String nome, String desc, String tipo,
                                      LocalDate inicio, LocalDate fim,
                                      double min, double max, double multiplo) {
        switch (tipo) {
            case "Eletrônico":
                return new LeilaoEletronico(nome, desc, inicio, fim, min, max, multiplo);
            case "Carta Fechada":
                return new LeilaoCartaFechada(nome, desc, inicio, fim, min);
            case "Venda Direta":
                return new LeilaoVendaDireta(nome, desc, inicio, fim, min);
            default:
                throw new IllegalArgumentException("Tipo de leilão inválido: " + tipo);
        }
    }

    private void associarLances(Leilao leilao, List<Integer> lancesIds, List<Lance> todosLances) {
        for (int id : lancesIds) {
            todosLances.stream()
                    .filter(l -> l.getId() == id)
                    .findFirst()
                    .ifPresent(leilao.getLances()::add);
        }
    }

    private void associarClientes(Leilao leilao, List<Integer> clientesIds, List<Cliente> todosClientes) {
        for (int id : clientesIds) {
            todosClientes.stream()
                    .filter(c -> c.getId() == id)
                    .findFirst()
                    .ifPresent(leilao.getClientesInscritos()::add);
        }
    }

    // Método para salvar leilões no ficheiro CSV
    public void salvarLeiloes(List<Leilao> leiloes) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            // Cabeçalho do arquivo CSV
            bw.write("id;nomeProduto;descricao;tipoLeilao;dataInicio;dataFim;valorMinimo;valorMaximo;multiploLance;lancesIds;clientesIds");
            bw.newLine();

            // Escreve cada leilão no arquivo
            for (Leilao leilao : leiloes) {
                String lancesIds = leilao.getLances().stream().map(l -> String.valueOf(l.getId())).collect(Collectors.joining(","));
                String clientesIds = leilao.getClientesInscritos().stream().map(c -> String.valueOf(c.getId())).collect(Collectors.joining(","));

                String linha = String.format(Locale.US,
                        "%d;%s;%s;%s;%s;%s;%.2f;%.2f;%.2f;%s;%s",
                        leilao.getId(),
                        leilao.getNomeProduto(),
                        leilao.getDescricao(),
                        leilao.getTipoLeilao(),
                        leilao.getDataInicio().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                        leilao.getDataFim().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                        leilao.getValorMinimo(),
                        leilao.getValorMaximo(),
                        leilao instanceof LeilaoEletronico ? ((LeilaoEletronico) leilao).getMultiploLance() : 0,
                        lancesIds,
                        clientesIds);
                bw.write(linha);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar o ficheiro Leiloes.csv: " + e.getMessage());
        }
    }

    // Método auxiliar para converter uma lista de IDs separada por vírgula em uma lista de inteiros
    private List<Integer> parseIdList(String ids) {
        List<Integer> lista = new ArrayList<>();
        if (!ids.isEmpty()) {
            String[] partes = ids.split(",");
            for (String parte : partes) {
                lista.add(Integer.parseInt(parte));
            }
        }
        return lista;
    }
}