package hibernate.tutorial;

import hibernate.tutorial.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.*;

import javax.naming.NamingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Hello world!
 */

@org.springframework.context.annotation.Configuration
@Import({EmbeddedServletContainerAutoConfiguration.class})
@EnableAutoConfiguration
@RestController
public class App {

    @Autowired
    private Session session;

    private Session tmp;

    @Bean
    public Session session() {
        Configuration config = new Configuration().configure();
        SessionFactory sessionFactory = config.buildSessionFactory();
        return sessionFactory.openSession();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Callable<List<User>> hello(@PathVariable Long id) {
        return () -> {
            List<User> result = new ArrayList<User>();
            if (id == null) {
                result.addAll(session.createQuery("from User").list());
            }
            result.add(session.get(User.class, id));
            Thread.currentThread().sleep(id * 1000);
            return result;
        };
    }

    @RequestMapping(value = "/createUser", method = RequestMethod.GET)
    public User createUser(@RequestParam("name") String name,
                           @RequestParam("surname") String surname,
                           @RequestParam("age") Long age,
                           @RequestParam(value = "login", required = false) String login) {
        User user = new User();

        if (login == null) {
            login = name + " " + surname;
        }
        user.setAge(age);
        user.setName(name);
        user.setSurname(surname);
        user.setLogin(login);
        session.saveOrUpdate(user);
        if(user.getId() != null) {
            return user;
        }
        return null;
    }

    @RequestMapping(value = "/session", method = RequestMethod.GET)
    public Boolean sessionChesh() {
        Boolean result = session == tmp;
        tmp = session;
        return result;
    }

    public static void main(String[] args) throws InterruptedException, NamingException {
        SpringApplication.run(App.class, args);
    }
}
