package vorona.andriy.controller;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by avorona on 29.12.15.
 */
@Controller
@CrossOrigin(methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE})
public class MainController {

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ResponseBody
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/sayhello", method = RequestMethod.GET)
    @ResponseBody
    public String hello(UserDetails userDetails) {
        return "Hello " + userDetails.getUsername();
    }
}
