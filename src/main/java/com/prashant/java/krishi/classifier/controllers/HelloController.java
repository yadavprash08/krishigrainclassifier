package com.prashant.java.krishi.classifier.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
public class HelloController {

    @RequestMapping("/ping")
    public String ping() {
        return "pong";
    }
}
