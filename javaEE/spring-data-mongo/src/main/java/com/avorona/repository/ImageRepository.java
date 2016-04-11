package com.avorona.repository;

import com.avorona.entity.Image;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * Created by avorona on 11.04.16.
 */
public interface ImageRepository extends MongoRepository<Image, Long> {

    List<Image> findByTitleLike(String titleLike);

    @Query("{title: {$regex: ?0}}")
    List<Image> findContainsTitle(String title);

    @Query("{originalImage: {$ne: null}}")
    List<Image> findByOriginalImage();
}
