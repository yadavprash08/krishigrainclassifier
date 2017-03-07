package com.prashant.java.krishi.classifier.repository;

import net.sf.javaml.classification.Classifier;
import net.sf.javaml.classification.KNearestNeighbors;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 *
 */
@Component
public class ClassifierFactory {

    @Resource private DataSetFactory dataSetFactory;

    private Classifier getClassifier() {
        Classifier c = new KNearestNeighbors(5);
        c.buildClassifier(dataSetFactory.getDataset());
        return c;
    }
}
