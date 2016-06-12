import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import vorona.andriy.model.City;
import vorona.andriy.model.House;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by avorona on 16.02.16.
 */

@Transactional
public class CityTest extends BaseTest {

  @Before
  @Transactional
  @Rollback(value = false)
  public void beforeTest() {
    cityRepository.deleteAll();

    Set<House> houses = new HashSet<>();
    houses.add(new House(1, "Volodymyrska"));
    houses.add(new House(2, "Lvivska"));

    City city1 = new City("Lutsk", 40);
    city1.getHouses().addAll(houses);

    City city2 = new City("Kyiv", 0);
    city2.getHouses().addAll(houses);

    cityRepository.save(city1);
    cityRepository.save(city2);
  }

  @Test
  @Transactional
  public void addNewHouses() {
    House newHouse = new House(3, "Kovelska");

    City city0 = cityRepository.findAll().get(0);

    int housesCount = city0.getHouses().size();

    logger.debug("Loaded city: " + city0);

    city0.getHouses().add(newHouse);

    cityRepository.save(city0);

    int newHousesCount = cityRepository.findOne(city0.getId()).getHouses().size();

    Assert.assertTrue("New house is not added", (newHousesCount - housesCount) == 1);
  }

  @Test
  @Transactional
  public void addOneHouse_forOneCity() {
    House newHouse = new House(3, "Kovelska");

    List<City> cityList = cityRepository.findAll();
    City city0 = cityList.get(0);
    City city1 = cityList.get(1);

    Assert.assertArrayEquals(city0.getHouses().toArray(), city1.getHouses().toArray());

    int housesCount0 = city0.getHouses().size();
    int housesCount1 = city1.getHouses().size();

    city0.getHouses().add(newHouse);

    cityRepository.save(city0);

    int newHousesCount0 = cityRepository.findOne(city0.getId()).getHouses().size();
    int newHousesCount1 = cityRepository.findOne(city1.getId()).getHouses().size();

    Assert.assertTrue("New house is added not only for first city", newHousesCount0 > newHousesCount1);
  }

  @Test
  @Transactional
  public void removeOneHouse_forOneCity() {
    List<City> allCities = cityRepository.findAll();
    City city0 = allCities.get(0);
    City city1 = allCities.get(1);

    Assert.assertArrayEquals(city0.getHouses().toArray(), city1.getHouses().toArray());

    int housesCount0 = city0.getHouses().size();
    int housesCount1 = city1.getHouses().size();

    city0.getHouses().remove(city0.getHouses().iterator().next());

    cityRepository.save(city0);

    int newHousesCount0 = cityRepository.findOne(city0.getId()).getHouses().size();
    int newHousesCount1 = cityRepository.findOne(city1.getId()).getHouses().size();

    Assert.assertTrue("House removed not only for one city", newHousesCount0 < newHousesCount1);
  }

  @Test
  @Transactional
  public void removeAllHouses_forOneCity() {
    List<City> allCities = cityRepository.findAll();
    City city0 = allCities.get(0);
    City city1 = allCities.get(1);

    Assert.assertArrayEquals(city0.getHouses().toArray(), city1.getHouses().toArray());

    int housesCount0 = city0.getHouses().size();
    int housesCount1 = city1.getHouses().size();

    city0.getHouses().clear();

    cityRepository.save(city0);

    int newHousesCount0 = cityRepository.findOne(city0.getId()).getHouses().size();
    int newHousesCount1 = cityRepository.findOne(city1.getId()).getHouses().size();

    Assert.assertTrue("All hoses were not removed from first city", newHousesCount0 == 0);
    Assert.assertTrue("Second city was affected by removing houses from first city", newHousesCount1 == housesCount1);
    Assert.assertTrue("House removed not only for one city", newHousesCount1 - newHousesCount0 == newHousesCount1);
  }

}
