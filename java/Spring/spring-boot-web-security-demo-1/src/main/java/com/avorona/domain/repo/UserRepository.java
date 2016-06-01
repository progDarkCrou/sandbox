package com.avorona.domain.repo;

import com.avorona.domain.model.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by avorona on 01.06.16.
 */
public interface UserRepository extends CrudRepository<User, Long> {
}
