package repositories;

import model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by avorona on 28.12.15.
 */
public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findAll();
}
