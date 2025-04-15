import Controller.NotificacaoController;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        NotificacaoController notificacaoController = new NotificacaoController();

        System.out.println("===== FORMULÁRIO DE REGISTO =====");

        System.out.print("Nome: ");
        String nomeDoUtilizador = scanner.nextLine();

        System.out.print("Email: ");
        String emailDoUtilizador = scanner.nextLine();

        // Verificação básica do formato do e-mail
        if (!emailDoUtilizador.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")) {
            System.out.println("Email inválido! Por favor, insira um e-mail válido.");
        } else {
            // Tenta enviar o e-mail
            boolean enviado = notificacaoController.enviarEmail(emailDoUtilizador, nomeDoUtilizador);

            if (enviado) {
                System.out.println("Registo concluído. Verifique o seu e-mail para a mensagem de boas-vindas!");
            } else {
                System.out.println("Ocorreu um erro ao enviar o e-mail. Tente novamente mais tarde.");
            }
        }

        scanner.close();
    }
}
