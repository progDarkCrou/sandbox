package com.avorona;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Created by avorona on 24.03.16.
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.httpBasic();
//        http.headers()
//                .defaultsDisabled()
//                .contentTypeOptions().and()
//                .httpStrictTransportSecurity().and();
//    }
}
