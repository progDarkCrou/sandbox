package vorona.andriy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import vorona.andriy.SessionHolder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by avorona on 29.12.15.
 */
@Controller
@CrossOrigin(methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE})
public class MainController {

    @Autowired
    private SessionHolder sessionHolder;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void login(Authentication authentication, HttpServletResponse response) {
        response.addCookie(new Cookie("X-SID", sessionHolder.generateAndSave((String) authentication.getPrincipal())));
    }

    @ResponseBody
    @RequestMapping(value = "/sayhello", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ADMIN')")
    public String hello(Authentication authentication) {
        return "Hello - " + authentication.getPrincipal();
    }
}
