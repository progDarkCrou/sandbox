package vorona.andriy.filter;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import vorona.andriy.filter.helper.InputStreamCloneServletRequestWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by avorona on 30.12.15.
 */
@Component
@Order(-1)
public class InputStreamCloneFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(new InputStreamCloneServletRequestWrapper((HttpServletRequest) servletRequest), servletResponse);
//        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
