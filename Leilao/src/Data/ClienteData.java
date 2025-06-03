package Data;

import Model.Cliente;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ClienteData {
    private static final String FILE_PATH = "C:\\Users\\Lenovo\\IdeaProjects\\TESTELP2\\Leilao\\data\\Cliente.csv";

    // Método para carregar clientes do ficheiro CSV
    public List<Cliente> carregarClientes() {
        List<Cliente> clientes = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String linha;
            br.readLine();

            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty()) continue; // Ignora linhas vazias

                String[] dados = linha.split(";");
                // Agora esperamos 10 campos, incluindo ultimoLogin
                if (dados.length < 10 || dados[0].trim().isEmpty()) {
                    System.out.println("Linha inválida ou incompleta: " + linha);
                    continue;
                }

                try {
                    int id = Integer.parseInt(dados[0].trim());
                    String nome = dados[1].trim();
                    String morada = dados[2].trim();
                    LocalDate dataNascimento = LocalDate.parse(dados[3].trim(), formatter);
                    String email = dados[4].trim();
                    String password = dados[5].trim();
                    int lancesDisponiveis = Integer.parseInt(dados[6].trim());
                    boolean isAdmin = Boolean.parseBoolean(dados[7].trim());
                    double saldo = Double.parseDouble(dados[8].trim().replace(",", "."));
                    LocalDate ultimoLogin = LocalDate.parse(dados[9].trim(), formatter);

                    Cliente cliente = new Cliente(nome, morada, dataNascimento, email, password, lancesDisponiveis, isAdmin, saldo);
                    cliente.setId(id);
                    cliente.setUltimoLogin(ultimoLogin);
                    clientes.add(cliente);

                } catch (Exception e) {
                    System.out.println("Erro ao processar linha: " + linha);
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o ficheiro Clientes.csv: " + e.getMessage());
        }

        return clientes;
    }

    // Método para salvar clientes no ficheiro CSV
    public static void salvarClientes(List<Cliente> clientes) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            // Cabeçalho atualizado para incluir ultimoLogin
            bw.write("id;nome;morada;dataNascimento;email;password;lancesDisponiveis;isAdmin;saldo;ultimoLogin");
            bw.newLine();

            for (Cliente cliente : clientes) {
                String linha = String.format("%d;%s;%s;%s;%s;%s;%d;%b;%.2f;%s",
                        cliente.getId(),
                        cliente.getNome(),
                        cliente.getMorada(),
                        cliente.getDataNascimento().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                        cliente.getEmail(),
                        cliente.getPassword(),
                        cliente.getLancesDisponiveis(),
                        cliente.isAdmin(),
                        cliente.getSaldo(),
                        cliente.getUltimoLogin().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                );

                bw.write(linha);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar o ficheiro Clientes.csv: " + e.getMessage());
        }
    }
}
