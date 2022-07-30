package demo.onlinebanking.configuration;

import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

public class MailConfiguration {

    public static JavaMailSenderImpl getMailConfiguration(){
        JavaMailSenderImpl jmailSender = new JavaMailSenderImpl();

        Properties properties = jmailSender.getJavaMailProperties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.debug.protocol", "true");

        jmailSender.setHost("smtp.mailtrap.io");
        jmailSender.setPort(2525);
        jmailSender.setUsername("e6be8d0d831df2");
        jmailSender.setPassword("ab7d71dfb89939");

        return jmailSender;
    }

}
