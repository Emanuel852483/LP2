package Controller;

import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.*;

public class NotificacaoController {

    public boolean enviarEmail(String destinatario, String nome) {

        final String SMTP_HOST = "smtp.gmail.com";
        final String remetenteEmail = "emanuelmaia75@gmail.com";
        final String password = "dtzg vcmm wcsy oewc"; // Usa uma app password segura

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", SMTP_HOST);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.ssl.trust", SMTP_HOST);

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remetenteEmail, password);
            }
        });

        try {
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
            return true;

        } catch (MessagingException e) {
            System.out.println("Erro ao enviar e-mail: " + e.getMessage());
            return false;
        }
    }
}
