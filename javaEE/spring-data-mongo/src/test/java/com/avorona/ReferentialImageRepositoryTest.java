package com.avorona;

import com.avorona.entity.Image;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Created by avorona on 11.04.16.
 */

public class ReferentialImageRepositoryTest extends ImageRepositoryTestBase {

    @Before
    public void fillWithReference() {
        repository.deleteWithOriginalImage();

        Image image = new Image();
        for (int i = 0; i < generatedCount; i++) {
            Image currentImage = dummyImage();
            currentImage.setOriginalImage(image);
            repository.save(currentImage);
            image = currentImage;
        }
    }

    @Test
    public void findWithOriginalImage() {
        final List<Image> withOriginalImage = repository.findByOriginalImage();

        withOriginalImage.stream().forEach(System.out::println);

        Assertions.assertThat(withOriginalImage).hasSize(generatedCount);
    }
}
