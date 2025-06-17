package Controller;

import Model.Cliente;
import Model.Leilao;
import jakarta.activation.DataHandler;
import jakarta.activation.FileDataSource;
import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.MessagingException;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;
import Data.ClienteData;
import jakarta.mail.internet.MimeMultipart;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class NotificacaoController {
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String remetenteEmail = "emanuelmaia75@gmail.com";
    private static final String password = "dtzg vcmm wcsy oewc";
    private static final String DIRETORIO = "C:\\Users\\Utilizador\\Desktop\\VERMVC\\data\\";


    private static Session criarSessaoEmail() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", SMTP_HOST);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.ssl.trust", SMTP_HOST);

        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remetenteEmail, password);
            }
        });
    }

    public void enviarEmail(String destinatario, String nome) {
        try {
            Session session = criarSessaoEmail();
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(remetenteEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject("Bem-vindo à nossa plataforma!");

            String htmlContent = "<!DOCTYPE html>"
                    + "<html><body style='font-family: Arial, sans-serif; background-color: #ffffff; padding: 20px; color: #333;'>"
                    + "<h2>Olá, " + nome + "!</h2>"
                    + "<p>Bem-vindo(a) à nossa plataforma.</p>"
                    + "<p>Obrigado por se registar. Estamos felizes por tê-lo(a) connosco.</p>"
                    + "<p>— Equipa de Suporte</p>"
                    + "</body></html>";

            message.setContent(htmlContent, "text/html; charset=utf-8");
            Transport.send(message);
            System.out.println("E-mail de boas-vindas enviado para: " + destinatario);
        } catch (MessagingException e) {
            System.out.println("Erro ao enviar e-mail: " + e.getMessage());
        }
    }

    private static void enviarEmailInatividade(String destinatario, String nome, LocalDateTime ultimoLogin) {
        if (ultimoLogin.plusMonths(3).isBefore(LocalDateTime.now())) {
            try {
                Session session = criarSessaoEmail();
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(remetenteEmail));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
                message.setSubject("Sentimos a sua falta! Volte à nossa plataforma!");

                String htmlContent = "<!DOCTYPE html>"
                        + "<html><body style='font-family: Arial, sans-serif; background-color: #ffffff; padding: 20px; color: #333;'>"
                        + "<h2>Olá, " + nome + "!</h2>"
                        + "<p>Reparámos que já passaram mais de 3 meses desde o seu último acesso.</p>"
                        + "<p>Sentimos a sua falta! Volte e veja as novidades que temos para si.</p>"
                        + "<p>— Equipa de Suporte</p>"
                        + "</body></html>";

                message.setContent(htmlContent, "text/html; charset=utf-8");
                Transport.send(message);
                System.out.println("E-mail de inatividade enviado para: " + destinatario);
            } catch (MessagingException e) {
                System.out.println("Erro ao enviar e-mail de inatividade: " + e.getMessage());
            }
        }
    }

    public static void verificarEEnviarEmailInatividade(String destinatario, String nome, LocalDateTime ultimoLogin) {
        if (ultimoLogin == null) {
            System.out.println("Último login desconhecido para " + nome + ", não envia email.");
            return;
        }

        if (ultimoLogin.plusMonths(3).isBefore(LocalDateTime.now())) {
            enviarEmailInatividade(destinatario, nome, ultimoLogin);
        } else {
            System.out.println("Cliente " + nome + " está ativo, não envia email.");
        }
    }

    public static void enviarEmailVencedorLeilao(String email, String nomeCliente, String nomeLeilao, double valorLance) {
        try {
            Session session = criarSessaoEmail();
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(remetenteEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Parabéns! Ganhou o leilão: " + nomeLeilao);

            String htmlContent = "<!DOCTYPE html>"
                    + "<html><body style='font-family: Arial, sans-serif; background-color: #ffffff; padding: 20px; color: #333;'>"
                    + "<h2>Olá, " + nomeCliente + "!</h2>"
                    + "<p>Parabéns! Foi o vencedor do leilão <strong>" + nomeLeilao + "</strong>.</p>"
                    + "<p>O valor final do seu lance foi de <strong>" + valorLance + "€</strong>.</p>"
                    + "<p>A nossa equipa entrará em contacto para concluir a transação.</p>"
                    + "<p>— Equipa de Suporte</p>"
                    + "</body></html>";

            message.setContent(htmlContent, "text/html; charset=utf-8");
            Transport.send(message);
        } catch (MessagingException e) {
            System.out.println("Erro ao enviar e-mail de vencedor: " + e.getMessage());
        }
    }

    public static void enviarEmailSemCreditos(String destinatario, String nome) {
        try {
            Session session = criarSessaoEmail();
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(remetenteEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject("Atenção: Ficou sem saldo!");

            String htmlContent = "<!DOCTYPE html>"
                    + "<html><body style='font-family: Arial, sans-serif; background-color: #ffffff; padding: 20px; color: #333;'>"
                    + "<h2>Olá, " + nome + "!</h2>"
                    + "<p>Verificámos que ficou sem saldo na sua conta.</p>"
                    + "<p>Para continuar a participar nos leilões, recomendamos que recarregue o seu saldo o mais breve possível.</p>"
                    + "<p>Se precisar de ajuda ou tiver dúvidas, entre em contacto connosco.</p>"
                    + "<p>— Equipa de Suporte</p>"
                    + "</body></html>";

            message.setContent(htmlContent, "text/html; charset=utf-8");
            Transport.send(message);

        } catch (MessagingException e) {
            System.out.println("Erro ao enviar e-mail de aviso de créditos: " + e.getMessage());
        }
    }

    private static void garantirDiretorioExiste() {
        File pasta = new File(DIRETORIO);
        if (!pasta.exists() && !pasta.mkdirs()) {
            System.err.println("Erro ao criar diretório: " + DIRETORIO);
        }
    }

    public static void gerarRelatoriosCSVSeparados(List<Leilao> leiloes, List<Cliente> clientes) {
        LocalDateTime agora = LocalDateTime.now();
        LocalDate hoje = agora.toLocalDate();
        LocalDate ontem = hoje.minusDays(1);

        garantirDiretorioExiste();

        // Leilões terminados ontem
        List<Leilao> leiloesTerminados = leiloes.stream()
                .filter(l -> l.getDataFim().toLocalDate().isEqual(ontem))
                .collect(Collectors.toList());
        escreverLeiloesCSV(leiloesTerminados, "leiloes_terminados.csv");

        // Leilões que começam hoje
        List<Leilao> leiloesHoje = leiloes.stream()
                .filter(l -> l.getDataInicio().toLocalDate().isEqual(hoje))
                .collect(Collectors.toList());
        escreverLeiloesCSV(leiloesHoje, "leiloes_a_comecar.csv");

        // Clientes pendentes ainda por confirmar
        List<Cliente> pendentes = clientes.stream()
                .filter(c -> "PENDENTE".equalsIgnoreCase(c.getStatus()))
                .collect(Collectors.toList());
        escreverClientesCSV(pendentes, "clientes_pendentes.csv");

        // Clientes que fizeram login ontem
        List<Cliente> loginsOntem = clientes.stream()
                .filter(c -> c.getUltimoLogin() != null &&
                        c.getUltimoLogin().toLocalDate().isEqual(ontem))
                .collect(Collectors.toList());
        escreverClientesLoginCSV(loginsOntem, "clientes_login_ontem.csv");
    }

    private static void escreverLeiloesCSV(List<Leilao> leiloes, String nomeFicheiro) {
        String caminhoCompleto = DIRETORIO + nomeFicheiro;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoCompleto))) {
            writer.write("ID,Produto,DataInicio,DataFim,ValorMinimo\n");
            for (Leilao l : leiloes) {
                writer.write(String.format("%d,%s,%s,%s,%.2f\n",
                        l.getId(),
                        l.getNomeProduto(),
                        l.getDataInicio().format(formatter),
                        l.getDataFim().format(formatter),
                        l.getValorMinimo()));
            }
        } catch (IOException e) {
            System.err.println("Erro ao escrever " + caminhoCompleto + ": " + e.getMessage());
        }
    }

    private static void escreverClientesCSV(List<Cliente> clientes, String nomeFicheiro) {
        String caminhoCompleto = DIRETORIO + nomeFicheiro;
        garantirDiretorioExiste(); // Garante que a pasta existe
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoCompleto))) {
            writer.write("ID,Nome,Email\n");
            for (Cliente c : clientes) {
                writer.write(String.format("%d,%s,%s\n", c.getId(), c.getNome(), c.getEmail()));
            }
        } catch (IOException e) {
            System.err.println("Erro ao escrever " + caminhoCompleto + ": " + e.getMessage());
        }
    }

    private static void escreverClientesLoginCSV(List<Cliente> clientes, String nomeFicheiro) {
        String caminhoCompleto = DIRETORIO + nomeFicheiro;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        garantirDiretorioExiste(); // Garante que a pasta existe
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoCompleto))) {
            writer.write("ID,Nome,Email,UltimoLogin\n");
            for (Cliente c : clientes) {
                writer.write(String.format("%d,%s,%s,%s\n",
                        c.getId(), c.getNome(), c.getEmail(),  c.getUltimoLogin().format(formatter)));
            }
        } catch (IOException e) {
            System.err.println("Erro ao escrever " + caminhoCompleto + ": " + e.getMessage());
        }
    }

    public static void enviarRelatoriosCSVparaGestor() {
        try {
            Session session = criarSessaoEmail();
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(remetenteEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(remetenteEmail));
            message.setSubject("Relatório Diário - Leiloeira " + LocalDate.now());

            // Corpo do email em HTML
            MimeBodyPart texto = new MimeBodyPart();
            String htmlContent = "<!DOCTYPE html>"
                    + "<html><body style='font-family: Arial, sans-serif; padding: 20px;'>"
                    + "<h2>Relatório Diário da Leiloeira</h2>"
                    + "<p>Segue em anexo os seguintes relatórios:</p>"
                    + "<ul>"
                    + "<li>Leilões terminados no dia anterior</li>"
                    + "<li>Leilões que começam hoje</li>"
                    + "<li>Clientes pendentes de confirmação</li>"
                    + "<li>Clientes que realizaram login ontem</li>"
                    + "</ul>"
                    + "<p>Os relatórios estão em formato CSV para fácil importação.</p>"
                    + "<p>Data do relatório: " + LocalDate.now() + "</p>"
                    + "</body></html>";
            texto.setContent(htmlContent, "text/html; charset=utf-8");

            MimeMultipart multipart = new MimeMultipart();
            multipart.addBodyPart(texto);

            // Anexos
            String[] ficheiros = {
                    "leiloes_terminados.csv",
                    "leiloes_a_comecar.csv",
                    "clientes_pendentes.csv",
                    "clientes_login_ontem.csv"
            };

            for (String nomeFicheiro : ficheiros) {
                String caminho = DIRETORIO + nomeFicheiro;
                File file = new File(caminho);
                if (file.exists()) {
                    MimeBodyPart anexo = new MimeBodyPart();
                    FileDataSource source = new FileDataSource(file);
                    anexo.setDataHandler(new DataHandler(source));
                    anexo.setFileName(nomeFicheiro);
                    multipart.addBodyPart(anexo);
                }
            }

            message.setContent(multipart);
            Transport.send(message);
        } catch (Exception e) {
            System.out.println("Erro ao enviar relatórios CSV: " + e.getMessage());
        }
    }
}
