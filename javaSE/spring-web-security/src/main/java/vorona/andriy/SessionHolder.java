package vorona.andriy;

import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.swing.plaf.DimensionUIResource;
import javax.xml.ws.ServiceMode;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by avorona on 01.03.16.
 */
@Service
public class SessionHolder {

    private static int sessionIdLength = 26;

    private Map<String, String> sessionsMap = new HashMap<>();

    public String generateAndSave(String username) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < sessionIdLength; i++) {
            int offsetToAdd = Math.round(Math.random()) == 0? 'a': 'A';
            builder.append(((int) (Math.random() * 26)) + offsetToAdd);
        }

        String sessionId = builder.toString();

        sessionsMap.put(username, sessionId);

        return sessionId;
    }

    public boolean containsSession(String sessionId, String username) {
        return sessionsMap.getOrDefault(username, "").equals(sessionId);
    }

    public boolean remove(String username) {
        sessionsMap.remove(username);
        return sessionsMap.containsKey(username);
    }

}
