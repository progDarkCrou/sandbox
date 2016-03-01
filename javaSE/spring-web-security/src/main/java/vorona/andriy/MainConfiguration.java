package vorona.andriy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import vorona.andriy.filter.AuthenticationFilter;

/**
 * Created by avorona on 28.12.15.
 */

@Configuration
@EnableWebMvc
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MainConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private SessionAuthenticationProvider demoAuthenticationProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .sessionManagement()
                    .disable()
                .headers()
                    .defaultsDisabled()
                    .contentTypeOptions()
                .and()
                    .cacheControl()
                        .disable()
                .and()
                    .logout()
                        .disable()
                .formLogin()
                    .disable()
                .anonymous()
                    .disable()
                .exceptionHandling()
                    .authenticationEntryPoint(new AuthenticationEntryPointImpl())
                .and()
                    .addFilterBefore(new AuthenticationFilter(), BasicAuthenticationFilter.class)
                    .authorizeRequests()
                        .antMatchers("/login")
                            .permitAll()
                        .anyRequest()
                            .authenticated();
    }


//    @Override
//    public void init(WebSecurity web) throws Exception {
//        web.addSecurityFilterChainBuilder(() -> {
//            return new DefaultSecurityFilterChain(new AntPathRequestMatcher("/**"), new AuthenticationFilter(),
//                    new SecurityContextHolderAwareRequestFilter(), new FilterSecurityInterceptor());
//        });
//    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(demoAuthenticationProvider);
    }

}
