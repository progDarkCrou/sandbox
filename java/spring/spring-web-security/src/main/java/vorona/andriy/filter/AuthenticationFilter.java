package vorona.andriy.filter;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import vorona.andriy.UsernameSessionToken;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by avorona on 25.02.16.
 */
public class AuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String username = request.getHeader("X-Auth-Username");
        String password = request.getHeader("X-Auth-Credential");

        Cookie session = request.getCookies() != null ? Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("X-SID"))
                .findFirst().get() : null;

        if (username != null && password != null) {
            Authentication authentication = new UsernameSessionToken(username, password, session != null ? session
                    .getValue() : null);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }


        filterChain.doFilter(request, response);
    }
}
