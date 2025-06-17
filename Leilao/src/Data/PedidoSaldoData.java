package Data;

import Model.PedidoSaldo;
import Model.Cliente;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PedidoSaldoData {
    private static final String FILE_PATH = "data/PedidoSaldo.csv";

    // Método para carregar pedidos de saldo do ficheiro CSV
    public static List<PedidoSaldo> carregarPedidos(List<Cliente> clientes) {
        List<PedidoSaldo> pedidos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String linha;
            br.readLine(); // Ignora o cabeçalho
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");
                int clienteId = Integer.parseInt(dados[0]);
                double quantia = Double.parseDouble(dados[1]);
                LocalDate dataPedido = LocalDate.parse(dados[2], DateTimeFormatter.ofPattern("dd-MM-yyyy"));

                // Trata o campo aprovado (que pode ser "null", "true" ou "false")
                Boolean aprovado = null;
                if (!dados[3].equalsIgnoreCase("null")) {
                    aprovado = Boolean.parseBoolean(dados[3]);
                }

                Cliente cliente = null;
                for (Cliente c : clientes) {
                    if (c.getId() == clienteId) {
                        cliente = c;
                        break;
                    }
                }

                if (cliente != null) {
                    PedidoSaldo pedido = new PedidoSaldo(cliente, quantia, dataPedido, aprovado);
                    pedidos.add(pedido);
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o ficheiro PedidoSaldo.csv: " + e.getMessage());
        }
        return pedidos;
    }

    public static void salvarPedidos(List<PedidoSaldo> pedidos) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            // Cabeçalho
            bw.write("clienteId;quantia;dataPedido;aprovado");
            bw.newLine();

            // Dados
            for (PedidoSaldo pedido : pedidos) {
                String linha = String.format(Locale.US,
                        "%d;%.2f;%s;%s",
                        pedido.getCliente().getId(),
                        pedido.getQuantia(),
                        pedido.getDataPedido().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                        pedido.isAprovado());
                bw.write(linha);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar o ficheiro PedidoSaldo.csv: " + e.getMessage());
        }
    }
}
