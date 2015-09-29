package hibernate.tutorial;

import hibernate.tutorial.model.User;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by avorona on 18.09.15.
 */
@RestController
@RequestMapping("/user")
@Transactional
public class UserController {

    @Autowired
    private Session session;

    private Session tmp;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<User> users() {
        List list = session.createQuery("from User").list();
        System.out.println("Find users: " + list);
        return list;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public User definedUser(@PathVariable Long id) {
        return session.get(User.class, id);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public User createUser(@RequestBody User user) {
        System.out.println(user);

        if (user.getLogin() == null) {
            user.setLogin(user.getName() + " " + user.getSurname());
        }

        session.saveOrUpdate(user);
        if (user.getId() != null) {
            return user;
        }
        return null;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public boolean removeUser(@PathVariable Long id) {
        User u = session.get(User.class, id);
        if (u != null) {
            session.delete(u);
            session.flush();
            System.out.println("Deleted user: " + u.getId());
            return session.get(User.class, id) == null;
        }
        return false;
    }
}
