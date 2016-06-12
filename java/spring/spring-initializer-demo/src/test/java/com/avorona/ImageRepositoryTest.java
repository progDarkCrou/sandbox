package com.avorona;

import com.avorona.web.ImageRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by avorona on 13.04.16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringInitializerDemoApplication.class)
public class ImageRepositoryTest {

    @Autowired
    private ImageRepository imageRepository;

    @Before
    public void beforeClean() {
        imageRepository.deleteAll();
    }

    @Test
    public void empty(){}
}
