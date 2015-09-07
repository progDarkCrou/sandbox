package hibernate.tutorial;

import hibernate.tutorial.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Map;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        Map<String, String> env = System.getenv();
        env.forEach((s, s2) -> {
            System.out.println("Key: " + s + " - value: " + s2);
            if (s.toLowerCase().equals("classpath")) {
                System.out.println("This is the class path of the application: " + s2);
            }
        });
        Configuration config = new Configuration().configure();
        SessionFactory sessionFactory = config.buildSessionFactory();
        Session session = sessionFactory.openSession();

//        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("User");
//        EntityManager entityManager = entityManagerFactory.createEntityManager();
        User user = new User();
        user.setAge(19);
        user.setName("Andriy");
        user.setLogin("crou");
        user.setSurname("Vorona");

//        entityManager.persist(user);

        session.saveOrUpdate(user);

        session.close();
        sessionFactory.close();
    }
}
