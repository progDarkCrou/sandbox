import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Properties;

/**
 * Created by avorona on 05.10.15.
 */
@Configuration
@ComponentScan(basePackages = {"web", "services", "model"})
@EnableWebMvc
@EnableAutoConfiguration
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    @Autowired
    @Profile(value = {"dev", "default"})
    public JavaMailSenderImpl mailSender(ConfigurableApplicationContext context) throws Exception {
        String mailSenderLoginPropName = "mail.sender.login";
        String mailSenderPassPropName = "mail.sender.password";

        String mailSenderLogin = context.getEnvironment().getProperty(mailSenderLoginPropName);
        String mailSenderPassword = context.getEnvironment().getProperty(mailSenderPassPropName);

        if (mailSenderLogin == null) {
            throw new Exception(mailSenderLoginPropName + " configuration property required");
        }
        if (mailSenderPassword == null) {
            throw new Exception(mailSenderPassPropName + " configuration property required");
        }

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(465);
        mailSender.setUsername(mailSenderLogin);
        mailSender.setPassword(mailSenderPassword);
        mailSender.setProtocol("smtps");

        Properties properties = new Properties();

        properties.setProperty("mail.smtps.auth", "true");
        properties.setProperty("mail.smtp.ssl.enable", "true");
        properties.setProperty("mail.transport.protocol", "smtps");
        mailSender.setJavaMailProperties(properties);

        return mailSender;
    }

    @Bean
    @Autowired
    public SimpleMailMessage templateMessage(JavaMailSenderImpl mailSender) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("default@vsa.io");
        return message;
    }
}
