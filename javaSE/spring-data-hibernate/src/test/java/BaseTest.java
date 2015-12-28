import model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import repositories.UserRepository;

import java.util.List;

/**
 * Created by avorona on 28.12.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MainConfiguration.class)
@Transactional
public class BaseTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void baseTest() {
        User user = new User("name", "surname");

        userRepository.save(user);

        List<User> userList = userRepository.findAll();

        Assert.assertTrue(userList.size() > 0);
    }
}
