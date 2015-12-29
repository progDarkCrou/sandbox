import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import vorona.andriy.MainConfiguration;
import vorona.andriy.model.House;
import vorona.andriy.model.User;
import vorona.andriy.repositories.HouseRepository;
import vorona.andriy.repositories.UserRepository;

import java.util.ArrayList;
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

    @Autowired
    private HouseRepository houseRepository;

    @Test
    public void baseTest() {
        List<House> houses = new ArrayList<>();
        houses.add(new House("volodymyrska", "Lutsk", 26));

        User user = new User("name", "surname");
        user.setHouses(houses);

        userRepository.save(user);

        List<User> userList = userRepository.findAll();
        Assert.assertTrue(userList.size() > 0);

        List<House> houses1 = houseRepository.findAll();

        houses1.stream().forEach(house -> {
            System.out.println(house.getUser());
        });
//        throw new RuntimeException();
    }
}
