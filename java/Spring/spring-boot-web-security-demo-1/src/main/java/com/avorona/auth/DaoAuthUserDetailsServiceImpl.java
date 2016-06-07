package com.avorona.auth;

import com.avorona.exception.UnableToFindException;
import com.avorona.service.UserService;
import org.glassfish.hk2.runlevel.RunLevelException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Created by avorona on 02.06.16.
 */
public class DaoAuthUserDetailsServiceImpl implements UserDetailsService {

    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return userService.find(username);
        } catch (UnableToFindException e) {
            throw new RunLevelException(e);
        }
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
