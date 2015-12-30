package vorona.andriy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vorona.andriy.model.Image;
import vorona.andriy.repositories.ImageRepository;

import javax.servlet.ServletException;
import javax.sql.rowset.serial.SerialBlob;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.sql.Blob;
import java.sql.SQLException;

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
    public String saveImage(InputStream inputStream, @RequestPart("file") MultipartFile file) throws IOException, ServletException, SQLException {
        ByteBuffer buffer = ByteBuffer.allocate(inputStream.available());
        Channels.newChannel(inputStream).read(buffer);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buffer.array())));
        reader.lines().forEach(System.out::println);
        Blob blob = new SerialBlob(file.getBytes());
        String name = "image" + (int) (Math.random() * 100);

        Image image = new Image(name, blob);
        imageRepository.save(image);
        return name;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{imageName}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable String imageName) throws SQLException, IOException {
        Image image = imageRepository.findOneByName(imageName);
        long length = image.getFile().length();
        return new ResponseEntity<>(image.getFile().getBytes(1, (int) length), HttpStatus.FOUND);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/newImage")
    @ResponseBody
    public void saveImage(InputStream stream) throws IOException, ServletException, SQLException {
//        if (entity == null) {
//            throw new RuntimeException("Cannot read from null entity value");
//        }
//        if (!entity.hasBody()) {
//            throw new RuntimeException("Cannot read from entity without body");
//        }
//        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(entity.getBody())));
//        reader.lines().forEach(System.out::println);
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        reader.lines().forEach(System.out::println);
    }

}
