package com.prashant.java.krishi.classifier.controllers;

import com.prashant.java.krishi.classifier.repository.ClassifierFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import javax.annotation.Resource;

/**
 *
 */
@RestController
public class ClassifierController {

    @Resource ClassifierFactory classifiers;

    @RequestMapping("/classify/ping")
    public String classify(){
        return "pong";
    }

    @RequestMapping("/classify/wheat")
    public String classifyInput(WebRequest request) {
        StringBuilder result = new StringBuilder("===== Result of Classification =====");

        return result.toString();
    }
}
