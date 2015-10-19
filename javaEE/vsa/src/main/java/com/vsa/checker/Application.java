package com.vsa.checker;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.vsa.checker.model.Checker;
import com.vsa.checker.model.RegisteredPerson;
import com.vsa.checker.services.ResultMailSender;
import com.vsa.checker.utils.InstallCert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

/**
 * Created by avorona on 05.10.15.
 */
@EnableWebMvc
@EnableJms
@SpringBootApplication
public class Application {

    public static final String QUEUE_NAME = "queue";

    @Autowired
    private ConfigurableApplicationContext context;

    public static void main(String[] args) throws Exception {
        InstallCert.main(new String[]{"polandonline.vfsglobal.com"});
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        System.out.println("Sending active message.");

        Channel sendingChannel = context.getBean("sendingChannel", Channel.class);
        sendingChannel.queueDeclare(QUEUE_NAME, false, false, false, null);
        sendingChannel.basicPublish("", QUEUE_NAME, null, "Hello RabbitMQ World!!!".getBytes());
        sendingChannel.close();
    }

    @Bean
    @Profile(value = {"dev", "default"})
    public JavaMailSenderImpl mailSender() throws Exception {
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

    @Bean
    @Scope("prototype")
    public Checker checker(String data, String url, String referer, String name, String email) {
        RegisteredPerson person = new RegisteredPerson(name, email);
        Checker checker = new Checker(data, url, referer);
        ResultMailSender sender = context.getBean(ResultMailSender.class, person, checker);
        checker.setResultMailSender(sender);
        return checker;
    }

    @Bean
    public ConfigurableEnvironment environment() {
        return context.getEnvironment();
    }

    @Bean
    public Channel sendingChannel() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        return connection.createChannel();
    }

    @Bean
    public Channel receivingChannel() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        return connection.createChannel();
    }
}
