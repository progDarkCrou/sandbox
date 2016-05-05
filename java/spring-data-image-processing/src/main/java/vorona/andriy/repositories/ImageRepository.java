package vorona.andriy.repositories;

import org.springframework.data.repository.CrudRepository;
import vorona.andriy.model.Image;

import java.util.List;

/**
 * Created by avorona on 29.12.15.
 */
public interface ImageRepository extends CrudRepository<Image, String> {

    List<Image> findAll();

    Image findOneByName(String name);

}
