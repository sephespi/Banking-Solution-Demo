package demo.onlinebanking.mail_messenger;

import demo.onlinebanking.configuration.MailConfiguration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class MailMessenger {

    private MailMessenger() {
    }
    public static void htmlMailMessenger(String from, String to, String subject, String body) throws MessagingException {

        JavaMailSender sender = MailConfiguration.getMailConfiguration();

        MimeMessage message = sender.createMimeMessage();

        MimeMessageHelper htmlMessage = new MimeMessageHelper(message, true);

        htmlMessage.setTo(to);
        htmlMessage.setFrom(from);
        htmlMessage.setSubject(subject);
        htmlMessage.setText(body, true);

        sender.send(message);

    }

}
