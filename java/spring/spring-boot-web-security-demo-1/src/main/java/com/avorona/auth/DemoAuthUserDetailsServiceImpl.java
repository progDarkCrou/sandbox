package com.avorona.auth;

import com.avorona.domain.model.Authority;
import com.avorona.domain.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by avorona on 01.06.16.
 */
public class DemoAuthUserDetailsServiceImpl implements UserDetailsService {

    private Map<String, User> users = new HashMap<>();

    @PostConstruct
    public void init() {
        System.out.println("User initialized");
        User user = new User();
        user.setPassword("password");
        user.setUsername("user");
        Set<Authority> authorities = new HashSet<>();
        authorities.add(new Authority("ROLE_USER"));
        authorities.add(new Authority("ROLE_ADMIN"));
        authorities.add(new Authority("ROLE_MODERATOR"));
        user.setAuthorities(authorities);
        users.put(user.getUsername(), user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return users.get(username);
    }
}
