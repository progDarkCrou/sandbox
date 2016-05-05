package vorona.andriy.repositories;

import org.springframework.data.repository.CrudRepository;
import vorona.andriy.model.City;

import java.util.List;

/**
 * Created by avorona on 16.02.16.
 */
public interface CityRepository extends CrudRepository<City, Long> {

  List<City> findAll();

}
