package mx.edu.utez.vehicleManager.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfiguration {

    @Value("${email.sender}")
    private String emailUser;

    @Value("${email.password}")
    private String emailPassword;

    @Value("${mail.host}")
    private String emailHost;

    @Value("${mail.port}")
    private Integer emailPort;

    @Bean
    public JavaMailSender getJavaMailSender() {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(emailHost);
        mailSender.setPort(emailPort);
        mailSender.setUsername(emailUser);
        mailSender.setPassword(emailPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

}
