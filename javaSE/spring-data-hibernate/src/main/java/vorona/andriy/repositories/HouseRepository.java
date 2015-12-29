package vorona.andriy.repositories;

import vorona.andriy.model.House;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by avorona on 29.12.15.
 */
@Transactional
public interface HouseRepository extends CrudRepository<House, Integer> {

    List<House> findAll();
}
