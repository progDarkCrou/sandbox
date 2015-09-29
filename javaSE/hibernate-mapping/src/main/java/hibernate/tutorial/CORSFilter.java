package hibernate.tutorial;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by avorona on 21.09.15.
 */
@Component
public class CORSFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("CORS filter done");
        HttpServletResponse resp = (HttpServletResponse) res;
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, OPTIONS");
        resp.setHeader("Access-Control-Max-Age", "3600");
        filterChain.doFilter(req, res);
    }

    @Override
    public void destroy() {

    }
}
