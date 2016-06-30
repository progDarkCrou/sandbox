package com.avorona;

import com.avorona.model.ContinentEntity;
import com.avorona.model.CountryEntity;
import com.avorona.model.DistrictEntity;
import org.hibernate.Session;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class ApplicationTests {

  @Autowired
  private Session session;

  @Test
  public void contextLoads() {
    DistrictEntity district = new DistrictEntity();
    district.setName("Volyn");

    CountryEntity country = new CountryEntity();
    country.setName("Ukraine");

    country.addDistrict(district);

    ContinentEntity continent = new ContinentEntity();
    continent.setName("Europe");
    country.setContinent(continent);

    session.persist(country);
    session.flush();

//    session.createQuery().;
//    Assertions.assertThat(continents).hasSize(1);
  }
}
