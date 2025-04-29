package Controller;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class NotificacaoController {

    public boolean enviarEmail(String destinatario, String nome) {

        final String SMTP_HOST = "smtp.gmail.com";
        final String remetenteEmail = "emanuelmaia75@gmail.com";
        final String password = "dtzg vcmm wcsy oewc";

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
            message.setSubject("Bem-vindo ao Nosso Serviço!");
            message.setText("Olá, " + nome + ",\n\nObrigado por se registar no nosso serviço! É um prazer tê-lo connosco.\n\nCom os melhores cumprimentos,\nEquipa de Suporte");

            Transport.send(message);
            System.out.println("E-mail de boas-vindas enviado para: " + destinatario);
            return true;
        } catch (MessagingException e) {
            System.out.println("Erro ao enviar e-mail: " + e.getMessage());
            return false;
        }
    }
}
