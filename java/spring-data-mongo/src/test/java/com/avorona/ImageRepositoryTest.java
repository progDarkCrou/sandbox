package com.avorona;

import com.avorona.entity.Image;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by avorona on 11.04.16.
 */

public class ImageRepositoryTest extends ImageRepositoryTestBase {

    @Before
    public void before() {
        repository.deleteAll();

        IntStream.range(0, generatedCount).parallel().forEach(value -> {
            Image image = dummyImage();
            repository.save(image);
            if (value % 10000 == 0) System.out.println("Inserted " + value + " values");
        });
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
