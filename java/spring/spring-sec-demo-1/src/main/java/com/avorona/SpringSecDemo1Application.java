package com.avorona;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@RestController
public class SpringSecDemo1Application {

    @RequestMapping(value = "/hello", produces = {"application/json"})
    public Map<String, String> hello() {
        Map<String, String> hello = new HashMap<>();

        hello.put("text", "Hello from the demo 1");
        return hello;
    }

	public static void main(String[] args) {
		SpringApplication.run(SpringSecDemo1Application.class, args);
	}

}
