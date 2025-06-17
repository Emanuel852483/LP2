package Data;

import Model.Cliente;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ClienteData {
    private static final String FILE_PATH = "data/Cliente.csv";

    // Método para carregar clientes do ficheiro CSV
    public static List<Cliente> carregarClientes() {
        List<Cliente> clientes = new ArrayList<>();
        int maxId = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String linha;
            br.readLine(); // Ignora o cabeçalho
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");
                int id = Integer.parseInt(dados[0]);
                if(id > maxId) {
                    maxId = id;
                }
                String nome = dados[1];
                String morada = dados[2];
                LocalDate dataNascimento = LocalDate.parse(dados[3], DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                String email = dados[4];
                String password = dados[5];
                int lancesDisponiveis = Integer.parseInt(dados[6]);
                boolean isAdmin = Boolean.parseBoolean(dados[7]);
                double saldo = Double.parseDouble(dados[8].replace(",","."));
                LocalDateTime ultimoLogin = LocalDateTime.parse(dados[9].trim(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
                String status = dados [10];

                // Cria o cliente
                Cliente cliente = new Cliente(nome, morada, dataNascimento, email, password, lancesDisponiveis, isAdmin, saldo, ultimoLogin,status);
                Cliente.setProximoId(maxId + 1);
                cliente.setUltimoLogin(ultimoLogin);
                clientes.add(cliente);
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o ficheiro Clientes.csv: " + e.getMessage());
        }
        return clientes;
    }

    // Método para salvar clientes no ficheiro CSV
    public static void salvarClientes(List<Cliente> clientes) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            // Cabeçalho CSV
            bw.write("id;nome;morada;dataNascimento;email;password;lancesDisponiveis;isAdmin;saldo;ultimoLogin;status");
            bw.newLine();

            for (Cliente cliente : clientes) {
                String linha = String.format("%d;%s;%s;%s;%s;%s;%d;%b;%.2f;%s;%s",
                        cliente.getId(),
                        cliente.getNome(),
                        cliente.getMorada(),
                        cliente.getDataNascimento().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                        cliente.getEmail(),
                        cliente.getPassword(),
                        cliente.getLancesDisponiveis(),
                        cliente.isAdmin(),
                        cliente.getSaldo(),
                        cliente.getUltimoLogin().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
                        cliente.getStatus()
                );

                bw.write(linha);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar o ficheiro Clientes.csv: " + e.getMessage());
        }
    }
}