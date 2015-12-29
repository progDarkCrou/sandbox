package vorona.andriy;

import org.junit.Assert;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import vorona.andriy.model.House;
import vorona.andriy.model.User;
import vorona.andriy.repositories.HouseRepository;
import vorona.andriy.repositories.UserRepository;

import java.util.List;

/**
 * Created by avorona on 28.12.15.
 */
@Configuration
@ComponentScan
@EnableTransactionManagement(mode = AdviceMode.ASPECTJ, proxyTargetClass = true)
@EnableAutoConfiguration
public class App {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(App.class, args);

        UserRepository userRepository = context.getBean("userRepository", UserRepository.class);
        HouseRepository houseRepository = context.getBean("houseRepository", HouseRepository.class);

        User user = new User("name", "surname");

//        userRepository.save(user);

        List<User> userList = userRepository.findAll();
        Assert.assertTrue(userList.size() > 0);
        user = userList.get(0);

        House house = new House("Volodymyrska", "Lutsk", (int) (Math.random() * 100));

        house.setUser(user);

        houseRepository.save(house);

        List<House> houses1 = houseRepository.findAll();

        houses1.stream().forEach(h -> {
            houseRepository.delete(house);
        });

        user.getHouses().stream().forEach(house1 ->  {
            System.out.println(house);
        });
    }
}
