import org.apache.log4j.Logger;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import vorona.andriy.repositories.CityRepository;
import vorona.andriy.repositories.HouseRepository;
import vorona.andriy.repositories.UserRepository;

/**
 * Created by avorona on 28.12.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestMainConfiguration.class)
public class BaseTest {

  protected Logger logger;

  {
    logger = Logger.getLogger(this.getClass());
  }

  @Autowired
  protected UserRepository userRepository;

  @Autowired
  protected HouseRepository houseRepository;

  @Autowired
  protected CityRepository cityRepository;
}
