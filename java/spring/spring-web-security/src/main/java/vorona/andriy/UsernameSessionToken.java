package vorona.andriy;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Created by avorona on 01.03.16.
 */
public class UsernameSessionToken extends UsernamePasswordAuthenticationToken {

    private String sessionId;

    public UsernameSessionToken(String username, String password, String sessionId) {
        super(username, password);
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
