package vorona.andriy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Created by avorona on 01.03.16.
 */
@Component
class SessionAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private SessionHolder sessionHolder;

    private List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernameSessionToken token = (UsernameSessionToken) authentication;

        boolean hasSession = sessionHolder.containsSession(token
                .getSessionId(), (String) authentication.getPrincipal());

        if (hasSession && authentication.getPrincipal().equals(authentication.getCredentials())) {
            return new UsernamePasswordAuthenticationToken(authentication.getName(), null, authorities);
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UsernameSessionToken.class.isAssignableFrom(aClass);
    }
}
