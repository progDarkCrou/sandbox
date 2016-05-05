package com.avorona.web;

import com.avorona.web.vo.ImageVO;
import com.avorona.web.vo.SizeVO;
import org.apache.commons.imaging.ImageFormat;
import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Sanselan;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by avorona on 12.04.16.
 */

@Controller
@RequestMapping("/image")
public class MainController {

    @Autowired
    private ImageRepository imageRepository;

    private Logger logger = Logger.getLogger(MainController.class);

    private float maxImageWidth = 1360F;

    private static Pattern imageTitlePattern = Pattern.compile("(image[a-z0-9]+)\\.[a-z]{2,}");

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ImageVO create(@RequestPart("image") Part imagePart,
                          @RequestPart(value = "size", required = false) SizeVO
            size, HttpServletResponse response) throws IOException {
        try (ByteArrayOutputStream bytes = new ByteArrayOutputStream()) {
            logger.info("Retrieved image by length: " + imagePart.getSize());
            IOUtils.copy(imagePart.getInputStream(), bytes);

            try {
                ImageInfo imageInfo = Sanselan.getImageInfo(bytes.toByteArray());
                ImageFormat imageFormat = imageInfo.getFormat();

                int width;
                int height;

                if (size == null) {
                    width = imageInfo.getWidth() > maxImageWidth ? (int) maxImageWidth : imageInfo.getWidth();
                    height = width == maxImageWidth && width != imageInfo.getWidth() ?
                            (int) (maxImageWidth / imageInfo.getWidth() * imageInfo.getHeight()) :
                            imageInfo.getHeight();
                } else {
                    width = size.getWidth();
                    height = size.getHeight();
                }

                BufferedImage scaledImage = new BufferedImage(width, height, imageInfo.getColorType());

                Graphics2D graphics = (Graphics2D) scaledImage.getGraphics();
                AffineTransform tx = new AffineTransform();
                tx.scale(width, height);

                graphics.drawImage(Sanselan.getBufferedImage(bytes.toByteArray()), 0, 0, width, height, null);
                graphics.transform(tx);
                graphics.dispose();

                Image image = new Image();
                image.setTitle("image" + scaledImage.hashCode());

                image.setExtension(imageFormat.extension.toLowerCase());

                ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
                ImageIO.write(scaledImage, imageFormat.extension, outBytes);

                image.setData(outBytes.toByteArray());

                imageRepository.save(image);

                return new ImageVO(image);
            } catch (ImageReadException e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return null;
            }
        } catch (IOException e) {
            logger.error("Something happened while retrieving image data", e);
        }
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        return null;
    }

    @RequestMapping(path = "/{title:image[a-z0-9]+\\.[a-z]{2,}}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getImage(@PathVariable String title, HttpServletRequest request) {
        Matcher matcher = imageTitlePattern.matcher(title);

        if (!matcher.matches()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        title = matcher.group(1);

        Image image = imageRepository.getOneByTitle(title);
        if (image != null && image.getData() != null) {
            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.valueOf("image/" + image.getExtension()));
            headers.setContentLength(image.getData().length);
            headers.set("Content-Disposition", "inline;filename=\"" + image.getTitle() + "\"");
            headers.set("X-Content-Type-Options", "nosniff");

            return new ResponseEntity<>(image.getData(), headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/all")
    @ResponseBody
    public List<ImageVO> getAllImages(HttpServletRequest request) {
        List<Image> all = imageRepository.findAll();
        return all.parallelStream()
                .map(ImageVO::new)
                .map(imageVO -> {
                    imageVO.setTitle("http://" + request.getLocalName()
                            + ":" + request.getLocalPort()
                            + "/image/" + imageVO.getTitle());
                    return imageVO;
                })
                .collect(Collectors.toList());
    }
}
