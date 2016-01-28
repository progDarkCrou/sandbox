package vorona.andriy.filter.helper;

import org.apache.log4j.Logger;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.util.Arrays;

/**
 * Created by avorona on 30.12.15.
 */
public class InputStreamCloneServletRequestWrapper extends HttpServletRequestWrapper {

    private static final Logger LOGGER = Logger.getLogger(InputStreamCloneServletRequestWrapper.class);
    private final byte[] bytes;


    public InputStreamCloneServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);

        BufferedReader reader = new BufferedReader(new InputStreamReader(new BufferedInputStream(request.getInputStream())));

        bytes = reader.lines().map(String::getBytes).reduce((bytes1, bytes2) -> {
            byte[] newArray = new byte[bytes1.length + bytes2.length];
            System.arraycopy(bytes1, 0, newArray, 0, bytes1.length);
            System.arraycopy(bytes2, 0, newArray, bytes1.length, bytes2.length);
            return newArray;
        }).get();

        LOGGER.info("Input stream cloned");
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final byte[] bytesCloned = Arrays.copyOf(bytes, bytes.length);

        InputStream inputStream = new ByteArrayInputStream(bytesCloned);

        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                try {
                    return inputStream.available() < 0;
                } catch (IOException e) {
                    return true;
                }
            }

            @Override
            public boolean isReady() {
                try {
                    return inputStream.available() > -1;
                } catch (IOException e) {
                    return false;
                }
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return inputStream.read();
            }
        };
    }
}
