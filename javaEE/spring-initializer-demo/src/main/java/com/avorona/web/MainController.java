package com.avorona.web;

import com.avorona.web.vo.ImageVO;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;

/**
 * Created by avorona on 12.04.16.
 */
@RestController
@RequestMapping("/image")
public class MainController {

    @Autowired
    private ImageRepository imageRepository;

    private Logger logger = Logger.getLogger("MainController");

    @RequestMapping(method = RequestMethod.POST)
    public ImageVO create(@RequestPart("image") Part imagePart, HttpServletResponse response) throws IOException {
        logger.info("Retrieved image by length: " + imagePart.getSize());
        Image image = new Image();
        try {
            image.setData(IOUtils.toByteArray(imagePart.getInputStream()));
            image.setTitle("image" + System.currentTimeMillis());
            image = imageRepository.save(image);
            return new ImageVO(image.getTitle());
        } catch (IOException e) {
            logger.error("Something happened while retrieving image data", e);
        }
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        return null;
    }

    @RequestMapping(method = RequestMethod.GET, produces = "image/*")
    public HttpEntity<?> getImage(@RequestParam String title) {
        Image image = imageRepository.getOneByTitle(title);

        if (image != null) {
            return new ResponseEntity<>(image.getData(), HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
