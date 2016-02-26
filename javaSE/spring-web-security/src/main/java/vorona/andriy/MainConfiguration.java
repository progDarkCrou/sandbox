package vorona.andriy;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import vorona.andriy.filter.AuthenticationFilter;

import java.util.Collections;
import java.util.List;

/**
 * Created by avorona on 28.12.15.
 */

@Configuration
@EnableWebMvc
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MainConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                    .exceptionHandling()
                    .authenticationEntryPoint(new AuthenticationEntryPointImpl())
                .and()
                    .addFilterBefore(new AuthenticationFilter(), BasicAuthenticationFilter.class)
                    .authorizeRequests()
                        .antMatchers("/login").permitAll()
                    .anyRequest()
                        .fullyAuthenticated()
                .and()
                    .headers()
                    .defaultsDisabled()
                    .contentTypeOptions();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(new DemoAuthenticationProvider());
    }

    private class DemoAuthenticationProvider implements AuthenticationProvider {
        private List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));

        @Override
        public Authentication authenticate(Authentication authentication) throws AuthenticationException {
            if (authentication.getPrincipal().equals(authentication.getCredentials())) {
                return new UsernamePasswordAuthenticationToken(authentication.getName(),
                        authentication.getPrincipal(),
                        authorities);
            }
            throw new BadCredentialsException("User nor found");
        }

        @Override
        public boolean supports(Class<?> aClass) {
            return UsernamePasswordAuthenticationToken.class.isAssignableFrom(aClass);
        }
    }
}
