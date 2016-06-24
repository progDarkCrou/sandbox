import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import vorona.andriy.MainConfiguration;
import vorona.andriy.model.House;
import vorona.andriy.model.User;
import vorona.andriy.repositories.HouseRepository;
import vorona.andriy.repositories.UserRepository;

import java.util.List;

/**
 * Created by avorona on 28.12.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MainConfiguration.class)
public class BaseTest {

    private Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HouseRepository houseRepository;

    @Test
    @Transactional
    @Commit
    public void baseTest() {
        User user = new User("name", "surname");

        user.getHouses().add(new House("volodymyrska", "Lutsk", (int) (Math.random() * 100)));
        user.getHouses().add(new House("volodymyrska", "Lutsk", (int) (Math.random() * 100)));
        user.getHouses().add(new House("volodymyrska", "Lutsk", (int) (Math.random() * 100)));
        user.getHouses().add(new House("volodymyrska", "Lutsk", (int) (Math.random() * 100)));

        userRepository.save(user);

        List<User> all = userRepository.findAll();

        Assert.assertTrue(all.size() > 0);

        List<House> houses = houseRepository.findAll();

        Assert.assertTrue("Houses for user were not saved.", houses.size() >= user.getHouses().size());

        logger.info("Removing houses - started");
        houseRepository.delete(user.getHouses().get(0));
        logger.info("Removing houses - ended");

        int size = userRepository.findOne(user.getId()).getHouses().size();
        logger.info("Size of the user's houses " + size);
        Assert.assertTrue("User's houses is not empty.", size == 3);
    }
}
