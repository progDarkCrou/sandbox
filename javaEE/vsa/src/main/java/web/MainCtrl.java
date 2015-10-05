package web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import services.CheckRunner;
import web.vo.InitializationVO;

/**
 * Created by avorona on 05.10.15.
 */
@RestController
public class MainCtrl {

    @Autowired
    private CheckRunner runner;

    @RequestMapping(value = "/initialize", method = RequestMethod.POST)
    public String initialize(@RequestBody(required = false) InitializationVO vo) {
        System.out.println(vo.getHello());
        return vo.getHello();
    }
}
