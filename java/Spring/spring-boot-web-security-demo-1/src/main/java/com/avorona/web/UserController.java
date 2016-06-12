package com.avorona.web;

import com.avorona.domain.model.User;
import com.avorona.exception.AlreadyDefined;
import com.avorona.exception.UnableToFindException;
import com.avorona.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Created by avorona on 01.06.16.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/user")
    public UserResponse userData(Authentication authentication) {
        UserResponse response = new UserResponse((User) authentication.getPrincipal());
        return response;
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public UserResponse create(@RequestBody UserRequest req) throws AlreadyDefined {
        User user = userService.create(req);
        return new UserResponse(user);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    public void delete(@RequestParam Long id) throws UnableToFindException {
        userService.delete(id);
    }

    @RequestMapping("/user/{id}")
    public UserResponse update(@RequestParam Long id, @RequestBody UserRequest userRequest) throws UnableToFindException {
        return new UserResponse(userService.update(id, userRequest));
    }
}
