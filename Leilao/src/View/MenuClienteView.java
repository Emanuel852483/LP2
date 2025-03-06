package View;

import java.util.Scanner;

public class MenuClienteView {
    private ClienteController controller;
    private Scanner scanner;

    public MenuClienteView (ClienteController controller) {
        this.controller = controller;
        this.scanner = new Scanner(System.in);
    }

    public void menu() {
        while (true) {
            System.out.println("1. Adicionar cliente");
            System.out.println("2. Listar cliente");
            System.out.println("3. Atualizar cliente");
            System.out.println("4. Remover cliente");
            System.out.println("5. Sair");
            System.out.print("Escolha um opção: ");
            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    adicionarCliente();
                    break;

                case 2:
                    ListarClientes();
                    break;

                case 3:
                    atualizarCliente();
                    break;

                case 4:
                    removerCliente();
                    break;

                case 5:
                    return;

                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private void adicionarCliente() {
        System.out.print("Insira o nome do cliente: ");
        String nome = scanner.nextLine();
        System.out.print("Insira a morada do cliente: ");
        String morada = scanner.nextLine();
        System.out.print("Insira a data de nascimento do cliente: ");
        String dataNascimento = scanner.nextLine();
        System.out.print("Insira o email do cliente: ");
        String email = scanner.nextLine();
        System.out.print("Insira a password: ");
        String password = scanner.nextLine();

        Cliente cliente = new Cliente(nome, morada, dataNascimento, email, password);
        controller.adicionarCliente(index, cliente);
        System.out.println("Cliente adicionado com sucesso!");
    }

    private void listarClientes() {
        System.out.print("Lista de clientes: ");
        for (int i = 0; i < controller.listarClientes().size(); i++) {
            Cliente cliente = controller.listarClientes().get(i);
            System.out.println(i + ": " +cliente.getNome());
        }
    }

    private void atualizarCliente() {
        System.out.print("Escolha o cliente a atualizar: ");
        int index = scanner.nextInt();

        System.out.print("Insira o novo nome do cliente: ");
        String nome = scanner.nextLine();
        System.out.print("Insira a nova morada do cliente: ");
        String morada = scanner.nextLine();
        System.out.print("Insira a nova data de nascimento do cliente: ");
        String dataNascimento = scanner.nextLine();
        System.out.print("Insira o novo email do cliente: ");
        String email = scanner.nextLine();
        System.out.print("Insira a nova password: ");
        String password = scanner.nextLine();

        Cliente cliente = new Cliente(nome, morada, dataNascimento, email, password);
        controller.atualizarCliente(index, cliente);
        System.out.println("Cliente adicionado com sucesso!");
    }

    private void removerCliente() {
        listarClientes();
        System.out.print("Escolha o cliente a remover: ");
        int index = scanner.nextInt();
        controller.removerCliente(index);
        System.out.println("Cliente removido com sucesso!");
    }
}
