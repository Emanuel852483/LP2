package Data;

import Controller.LeilaoController;
import Model.*;

import java.time.LocalDateTime;
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
    public List<Leilao> carregarLeiloes(List<Cliente> clientes, List<Lance> lances, List<AvaliacaoLeilao> avaliacoes) {
        List<Leilao> leiloes = new ArrayList<>();
        int maxId = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String linha;
            br.readLine(); // Ignora o cabeçalho
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";", -1); // O -1 mantem campos vazios

                try {
                    int id = Integer.parseInt(dados[0]);
                    if (id > maxId) {
                        maxId = id;
                    }
                    String nomeProduto = dados[1];
                    String descricao = dados[2];
                    String tipoLeilao = dados[3];
                    LocalDateTime dataInicio = LocalDate.parse(dados[4], DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")).atStartOfDay();
                    LocalDateTime dataFim = LocalDate.parse(dados[5], DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")).atStartOfDay();
                    double valorMinimo = Double.parseDouble(dados[6].replace(",", "."));
                    double multiploLance = !dados[7].isEmpty() ? Double.parseDouble(dados[7].replace(",", ".")) : 0.0;
                    boolean isAtivo = Boolean.parseBoolean(dados[8]);
                    boolean isFechado = Boolean.parseBoolean(dados[9]);
                    List<Integer> lancesIds = !dados[10].isEmpty() ? parseIdList(dados[10]) : new ArrayList<>();
                    List<Integer> clientesIds = !dados[11].isEmpty() ? parseIdList(dados[11]) : new ArrayList<>();
                    Integer vencedorId = !dados[12].isEmpty() ? Integer.parseInt(dados[12]) : null;
                    List<Integer> avaliacoesIds = !dados[13].isEmpty() ? parseIdList(dados[13]) : new ArrayList<>();

                    // Cria o leilão
                    Leilao leilao = criarLeilaoPorTipo(nomeProduto, descricao, tipoLeilao,
                            dataInicio, dataFim, valorMinimo, isAtivo, isFechado, multiploLance);
                    leilao.setId(id);

                    // Associa lances, clientes e vencedor
                    associarLances(leilao, lancesIds, lances);
                    associarClientes(leilao, clientesIds, clientes);
                    associarAvaliacoes(leilao, avaliacoesIds, avaliacoes);

                    if (vencedorId != null) {
                        for (Cliente cliente : clientes) {
                            if (cliente.getId() == vencedorId) {
                                leilao.setVencedor(cliente);
                                break;
                            }
                        }
                    }

                    leiloes.add(leilao);
                } catch (Exception e) {
                    System.out.println("Erro ao processar linha: " + linha);
                    e.printStackTrace();
                }
            }
            Leilao.setProximoId(maxId + 1);
        } catch (IOException e) {
            System.out.println("Erro ao ler o ficheiro Leiloes.csv: " + e.getMessage());
        }
        return leiloes;
    }

    private Leilao criarLeilaoPorTipo(String nome, String desc, String tipo,
                                      LocalDateTime inicio, LocalDateTime fim,
                                      double min, boolean isativo, boolean isfechado, double multiplo) {
        switch (tipo) {
            case "Eletrônico":
                return new LeilaoEletronico(nome, desc, inicio, fim, min, multiplo, isativo, isfechado);
            case "Carta Fechada":
                return new LeilaoCartaFechada(nome, desc, inicio, fim, min, isativo, isfechado);
            case "Venda Direta":
                return new LeilaoVendaDireta(nome, desc, inicio, fim, min, isativo, isfechado);
            default:
                throw new IllegalArgumentException("Tipo de leilão inválido: " + tipo);
        }
    }

    private void associarLances(Leilao leilao, List<Integer> lancesIds, List<Lance> todosLances) {
        for (int id : lancesIds) {
            for (Lance lance : todosLances) {
                if (lance.getId() == id) {
                    leilao.getLances().add(lance);
                    break;
                }
            }
        }
    }

    private void associarAvaliacoes(Leilao leilao, List<Integer> avaliacoesIds, List<AvaliacaoLeilao> todasAvaliacoes) {
        for (int id : avaliacoesIds) {
            for (AvaliacaoLeilao avaliacaoLeilao : todasAvaliacoes) {
                if (avaliacaoLeilao.getId() == id) {
                    leilao.getAvaliacoesdosclientes().add(avaliacaoLeilao);
                    break;
                }
            }
        }
    }

    private void associarClientes(Leilao leilao, List<Integer> clientesIds, List<Cliente> todosClientes) {
        for (int id : clientesIds) {
            for (Cliente cliente : todosClientes) {
                if (cliente.getId() == id) {
                    leilao.getClientesInscritos().add(cliente);
                    break;
                }
            }
        }
    }



    // Método para salvar leilões no ficheiro CSV
    public static void salvarLeiloes(List<Leilao> leiloes) {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            // Cabeçalho do arquivo CSV
            bw.write("id;nomeProduto;descricao;tipoLeilao;dataInicio;dataFim;valorMinimo;multiploLance;isAtivo;isFechado;lancesIds;clientesIds;vencedorId;Avaliacoesdosclientes");
            bw.newLine();



            // Escreve cada leilão no arquivo
            for (Leilao leilao : leiloes) {
                String lancesIds = leilao.getLances().stream().map(l -> String.valueOf(l.getId())).collect(Collectors.joining(","));
                String clientesIds = leilao.getClientesInscritos().stream().map(c -> String.valueOf(c.getId())).collect(Collectors.joining(","));
                String vencedorId = leilao.getVencedor() != null ? String.valueOf(leilao.getVencedor().getId()) : "";
                String avaliacoesclientes = leilao.getAvaliacoesdosclientes().stream().map(a -> String.valueOf(a.getId())).collect(Collectors.joining(","));



                String linha = String.format(Locale.US,
                        "%d;%s;%s;%s;%s;%s;%.2f;%.2f;%b;%b;%s;%s;%s;%s",
                        leilao.getId(),
                        leilao.getNomeProduto(),
                        leilao.getDescricao(),
                        leilao.getTipoLeilao(),
                        leilao.getDataInicio().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
                        leilao.getDataFim().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
                        leilao.getValorMinimo(),
                        leilao instanceof LeilaoEletronico ? ((LeilaoEletronico) leilao).getMultiploLance() : 0,
                        leilao.isAtivo(),
                        leilao.isFechado(),
                        lancesIds.isEmpty() ? "" : lancesIds,
                        clientesIds.isEmpty() ? "" : clientesIds,
                        vencedorId,
                        avaliacoesclientes.isEmpty() ? "" : avaliacoesclientes);
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