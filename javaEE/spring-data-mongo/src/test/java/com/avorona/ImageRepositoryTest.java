package com.avorona;

import com.avorona.entity.Image;
import com.avorona.repository.ImageRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by avorona on 11.04.16.
 */

public class ImageRepositoryTest extends ImageRepositoryTestBase {

    @Before
    public void before() {
        repository.deleteAll();
        for (int i = 0; i < generatedCount; i++) {
            Image image = dummyImage();
            repository.save(image);
        }
    }

    @Test
    public void findAll() {
        assertThat(repository.count()).isEqualTo(generatedCount);
    }

    @Test
    public void findImageByTitle() {
        final List<Image> byTitleImages = repository.findByTitleLike("title");
        assertThat(byTitleImages).hasSize(generatedCount);
    }

    @Test
    public void findContainsTitle() {
        assertThat(repository.findContainsTitle(".*title.*").size()).isEqualTo(generatedCount);
    }
}
