package Data;

import Model.AvaliacaoLeilao;
import Model.Cliente;
import Model.Leilao;
import Model.Nota;

import java.io.*;
import java.util.*;


public class AvaliacaoLeilaoData {
    private static final String FILE_PATH = "data/AvaliacaoLeilao.csv";

    // Carrega avaliações do ficheiro CSV
    public static List<AvaliacaoLeilao> carregarAvaliacoes(List<Cliente> clientes, List<Leilao> leiloes,List<Nota> notas) {
        List<AvaliacaoLeilao> avaliacoes = new ArrayList<>();
        int maxId = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String linha;
            br.readLine(); // Ignora o cabeçalho
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";", -1); // O -1 mantem campos vazios
                int id = Integer.parseInt(dados[0]);
                if(id > maxId) {
                    maxId = id;
                }
                int clienteId = Integer.parseInt(dados[1]);
                int leilaoId = Integer.parseInt(dados[2]);
                int notaId = Integer.parseInt(dados[3]);
                String comentario = dados[4];


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

                Nota nota = null;
                for (Nota n : notas) {
                    if (n.getId() == notaId) {
                        nota = n;
                        break;
                    }
                }


                if (cliente != null && leilao != null) {
                    AvaliacaoLeilao avaliacao = new AvaliacaoLeilao(cliente,leilao, nota, comentario);
                    avaliacoes.add(avaliacao);
                    avaliacao.setId(id);
                }
            }
            AvaliacaoLeilao.setProximoId(maxId + 1);
        } catch (IOException e) {
            System.out.println("Erro ao ler o ficheiro AvaliacaoLeilao.csv: " + e.getMessage());
        }
        return avaliacoes;
    }

    // Salva avaliações no ficheiro CSV
    public static void salvarAvaliacoes(List<AvaliacaoLeilao> avaliacoes) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            // Cabeçalho
            bw.write("id;clienteId;leilaoId;nota;comentario");
            bw.newLine();

            // Dados
            for (AvaliacaoLeilao avaliacao : avaliacoes) {
                String linha = String.format("%d;%d;%d;%d;%s",
                        avaliacao.getId(),
                        avaliacao.getCliente().getId(),
                        avaliacao.getLeilao().getId(),
                        avaliacao.getNota().getId(),
                        avaliacao.getComentario());
                bw.write(linha);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar o ficheiro AvaliacaoLeilao.csv: " + e.getMessage());
        }
    }
}