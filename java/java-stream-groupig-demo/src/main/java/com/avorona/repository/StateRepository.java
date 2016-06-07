package com.avorona.repository;

import com.avorona.model.State;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by avorona on 13.04.16.
 */
public interface StateRepository extends MongoRepository<State, BigInteger> {
    List<State> findAll();
}
