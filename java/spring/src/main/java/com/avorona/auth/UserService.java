package com.avorona.auth;

import com.avorona.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by avorona on 30.05.16.
 */

public class UserService implements UserDetailsService {

    private Map<String, User> users = new HashMap<>();

    private void init() {
        String username = "user";
        User user = new User(username, "ChangePass", "Andriy", "Vorona");
        user.addAuthority("ROLE_ADMIN");
        this.users.put(username, user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return users.get(username);
    }
}
