package vorona.andriy.filter.helper;

import org.apache.commons.io.IOUtils;
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

        bytes = IOUtils.toByteArray(super.getInputStream());

        LOGGER.info("Input stream cloned");
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final byte[] bytesCloned = Arrays.copyOf(bytes, bytes.length);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytesCloned);

        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return true;
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
