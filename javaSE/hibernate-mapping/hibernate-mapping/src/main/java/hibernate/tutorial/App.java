package hibernate.tutorial;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.naming.NamingException;

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
    @Profile(value = "dev")
    public Session session() {
        Configuration config = new Configuration().configure();
        SessionFactory sessionFactory = config.buildSessionFactory();
        return sessionFactory.openSession();
    }

    public static void main(String[] args) throws InterruptedException, NamingException {
        SpringApplication.run(App.class, args);
    }
}
