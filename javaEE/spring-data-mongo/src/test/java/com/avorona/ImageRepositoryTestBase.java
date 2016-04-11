package com.avorona;

import com.avorona.entity.Image;
import com.avorona.repository.ImageRepository;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;

/**
 * Created by avorona on 11.04.16.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfig.class)
@Ignore
public class ImageRepositoryTestBase {

    final int generatedCount = 10;

    @Autowired
    protected ImageRepository repository;

    public static Image dummyImage() {
        return new Image("title" + LocalDateTime.now().getNano(), "JPG");
    }
}
