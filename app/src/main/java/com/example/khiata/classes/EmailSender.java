package com.example.khiata.classes;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender {

    // ExecutorService para rodar em uma thread separada
    private static ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static void enviarEmail(final String emailDestino, final String assunto, final String mensagem) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                // Configurações para o envio do e-mail
                final String username = "hcarvalhocampos2008@gmail.com";
                final String password = "@Mamute@2008";

                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");

                // Cria a sessão com autenticação
                Session session = Session.getInstance(props,
                        new javax.mail.Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(username, password);
                            }
                        });

                try {
                    // Cria a mensagem de e-mail
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(username));
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailDestino));
                    message.setSubject(assunto);
                    message.setText(mensagem);

                    // Envia o e-mail
                    Transport.send(message);
                    System.out.println("E-mail enviado com sucesso!");

                } catch (MessagingException e) {
                    e.printStackTrace(); // Mostra o erro no console
                    System.err.println("Falha ao enviar o e-mail: " + e.getMessage());
                }
            }
        });
    }
}
