package vorona.andriy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vorona.andriy.model.Image;
import vorona.andriy.repositories.ImageRepository;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.serial.SerialBlob;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by avorona on 29.12.15.
 */
@Controller
@CrossOrigin(methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE})
public class MainController {

  @Autowired
  private ImageRepository imageRepository;

  //
  @RequestMapping(method = RequestMethod.POST, value = "/image")
  @ResponseBody
  public String saveImage(@RequestPart("file") MultipartFile file) throws IOException, ServletException, SQLException {
    Blob blob = new SerialBlob(file.getBytes());
    String name = "image" + (int) (Math.random() * 100);

    Image image = new Image(name, blob);
    imageRepository.save(image);
    return name;
  }

  @RequestMapping(method = RequestMethod.GET, value = "/{imageName}.jpg")
  public ResponseEntity<?> getImage(@PathVariable String imageName,
                                    @RequestParam(required = false) Integer width,
                                    @RequestParam(required = false) Integer height,
                                    @RequestParam(required = false) String todo,
                                    HttpServletRequest request)
          throws SQLException, IOException {

    Image image = imageRepository.findOneByName(imageName);

    if (image == null) {
      try {
        return ResponseEntity.notFound()
                .location(new URI("http://" + request.getRemoteHost() + "/allImages"))
                .build();
      } catch (URISyntaxException e) {
        e.printStackTrace();
        return null;
      }
    }

    Blob file = image.getFile();

    long length = file.length();

    BufferedImage resultImage;
    if (todo != null && width != null && height != null) {
      BufferedImage img = ImageIO.read(new BufferedInputStream(file.getBinaryStream()));
      resultImage = new BufferedImage(width, height, img.getType());
      Graphics2D graphics = resultImage.createGraphics();

      switch (todo.toLowerCase()) {
        case "size": {
          graphics.drawImage(img, 0, 0, width, height, null);
          graphics.dispose();
          break;
        }
        case "cover": {
          double initialRatio = ((double) img.getWidth()) / img.getHeight();
          double destRatio = width.doubleValue() / height.doubleValue();
          double leftCornerX = 0;
          double leftCornerY = 0;

          double destWidth = img.getWidth();
          double destHeight = img.getHeight();

          if (initialRatio > destRatio) {
            destWidth = img.getHeight() * destRatio;
            leftCornerX = (img.getWidth() - destWidth) / 2;
          } else {
            destHeight = img.getWidth() / destRatio;
            leftCornerY = (img.getHeight() - destHeight) / 2;
          }

          resultImage = new BufferedImage(((int) destWidth), ((int) destHeight), BufferedImage.TYPE_INT_RGB);
          graphics = resultImage.createGraphics();

          graphics.drawImage(img, 0, 0, (int) destWidth, (int) destHeight, (int) leftCornerX,
                  (int) leftCornerY, (int) (destWidth + leftCornerX), (int) (destHeight + leftCornerY), null);
          graphics.dispose();

          break;
        }
        case "crop": {
          return null;
        }
      }

    } else {
      resultImage = ImageIO.read(file.getBinaryStream());
    }

    ByteArrayOutputStream out = new ByteArrayOutputStream();

    ImageIO.write(resultImage, "png", out);

    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "image/jpeg");
    headers.add("Cache-Control", "private, max-age=604800");

    return new ResponseEntity<>(out.toByteArray(), headers, HttpStatus.FOUND);
  }

  @RequestMapping(method = RequestMethod.GET, value = "/allImages")
  @ResponseBody
  public Collection<String> allImages() {
    return imageRepository.findAll().parallelStream().map(Image::getName).collect(Collectors.toList());
  }

}
