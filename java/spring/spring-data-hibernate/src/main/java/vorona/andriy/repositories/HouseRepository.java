package vorona.andriy.repositories;

import org.springframework.data.repository.CrudRepository;
import vorona.andriy.model.House;

import java.util.List;

/**
 * Created by avorona on 29.12.15.
 */
public interface HouseRepository extends CrudRepository<House, Integer> {

    List<House> findAll();
}
