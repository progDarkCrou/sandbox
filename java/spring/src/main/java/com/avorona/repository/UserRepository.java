package com.avorona.repository;

import com.avorona.model.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by avorona on 30.05.16.
 */
public interface UserRepository extends CrudRepository<User, Long> {
}
