package com.avorona.web;

import com.avorona.domain.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by avorona on 01.06.16.
 */
@RestController
@RequestMapping("/user")
public class LoginController {

    @RequestMapping("/user")
    public UserResponse userData(Authentication authentication) {
        UserResponse response = new UserResponse((User) authentication.getPrincipal());
        return response;
    }
}
