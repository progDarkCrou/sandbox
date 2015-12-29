package vorona.andriy.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import vorona.andriy.model.User;

import java.util.List;

/**
 * Created by avorona on 28.12.15.
 */
@Transactional
public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findAll();
}
