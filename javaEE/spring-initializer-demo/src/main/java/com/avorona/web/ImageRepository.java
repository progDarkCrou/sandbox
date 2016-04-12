package com.avorona.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.math.BigInteger;

/**
 * Created by avorona on 12.04.16.
 */
public interface ImageRepository extends PagingAndSortingRepository<Image, BigInteger> {

    @Audit
    Image getOneByTitle(String title);
}
