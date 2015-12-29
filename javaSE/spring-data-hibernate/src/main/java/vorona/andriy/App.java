package vorona.andriy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by avorona on 28.12.15.
 */
@Configuration
@ComponentScan
@EnableWebMvc
@EnableTransactionManagement(mode = AdviceMode.ASPECTJ, proxyTargetClass = true)
@EnableAutoConfiguration
public class App {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(App.class, args);
    }
}
