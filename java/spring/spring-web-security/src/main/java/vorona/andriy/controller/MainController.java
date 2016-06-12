package vorona.andriy.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by avorona on 29.12.15.
 */
@RestController
@CrossOrigin(methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE})
public class MainController {

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public Map<String, String> user(Authentication auth) {
        Map<String, String> userDetails = new TreeMap<>();
        userDetails.put("username", auth.getPrincipal().toString());
        return userDetails;
    }
}
