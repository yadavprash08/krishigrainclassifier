package com.prashant.java.krishi.classifier.controllers;

import com.prashant.java.krishi.classifier.modal.WebRequestUtil;
import com.prashant.java.krishi.classifier.modal.WheatDimension;
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
    public String classify() {
        return "pong";
    }

    @RequestMapping("/classify/wheat")
    public String classifyInput(WebRequest request) {
//        WebRequestUtil utilRequest = new WebRequestUtil(request);
        StringBuilder result = new StringBuilder("===== Result of Classification =====");
//        WheatDimension dimensions = new WheatDimension(utilRequest);
//        result.append("Dimensions :: " + dimensions.toString() + "\n");
//        Object classification = classifiers.getClassifier().classify(dimensions.instance());
//        result.append("Classified as :: " + classification.toString() + "\n");
        return result.toString();
    }
}
