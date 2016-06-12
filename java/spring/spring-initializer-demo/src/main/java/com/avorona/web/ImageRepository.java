package com.avorona.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by avorona on 12.04.16.
 */
public interface ImageRepository extends PagingAndSortingRepository<Image, BigInteger> {

    Image getOneByTitle(String title);

    @Override
    List<Image> findAll();
}
