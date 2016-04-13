package com.avorona.repository;

import com.avorona.model.Country;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by avorona on 13.04.16.
 */
public interface CountryRepository extends MongoRepository<Country, BigInteger> {
    List<Country> findAll();
}
