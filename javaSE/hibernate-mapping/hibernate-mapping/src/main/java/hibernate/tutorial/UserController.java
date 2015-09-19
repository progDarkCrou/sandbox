package hibernate.tutorial;

import hibernate.tutorial.model.User;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by avorona on 18.09.15.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private Session session;

    private Session tmp;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Callable<List<User>> users() {
        return () -> {
            Thread.currentThread().sleep((long) (Math.random() * 1000));
            return session.createQuery("from User").list();
        };
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Callable<User> definedUser(@PathVariable Long id) {
        return () -> session.get(User.class, id);
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
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
}
