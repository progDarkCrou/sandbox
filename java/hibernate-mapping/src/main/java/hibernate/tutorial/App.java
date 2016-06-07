package hibernate.tutorial;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.naming.NamingException;
import javax.transaction.TransactionManager;
import java.util.Arrays;

/**
 * Hello world!
 */

@EnableWebMvc
@ComponentScan
@EnableAutoConfiguration
@Import({EmbeddedServletContainerAutoConfiguration.class})
@org.springframework.context.annotation.Configuration
public class App {

    @Bean
    @Profile(value = {"dev", "hell"})
    public Session session() {
        Configuration config = new Configuration().configure();
        SessionFactory sessionFactory = config.buildSessionFactory();
        return sessionFactory.openSession();
    }

    @Bean
    public TransactionManager transactionManager () {
        return  null;
    }

    public static void main(String[] args) throws InterruptedException, NamingException {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(App.class, args);
        StandardEnvironment environment = new StandardEnvironment();

        while (true) {
            Thread.currentThread().sleep((long) (Math.random() * 1000));
            String devProfile = "dev";
            if (Arrays.binarySearch(environment.getActiveProfiles(), devProfile) != -1) {
                environment.setActiveProfiles(devProfile);
            } else {
                environment.setActiveProfiles("hell");
            }
            applicationContext.setEnvironment(environment);
            applicationContext.stop();
            applicationContext.start();
        }
    }
}
