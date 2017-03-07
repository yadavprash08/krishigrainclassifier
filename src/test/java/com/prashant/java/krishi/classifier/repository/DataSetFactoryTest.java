package com.prashant.java.krishi.classifier.repository;

import net.sf.javaml.classification.Classifier;
import net.sf.javaml.classification.KNearestNeighbors;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

/**
 *
 */
public class DataSetFactoryTest {
    DataSetFactory dataSetFactory = new DataSetFactory();

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void loadDataSet() throws Exception {
        dataSetFactory.loadDataSet();
        final Dataset dataSet = dataSetFactory.getDataset();
        dataSet.forEach(System.out::println);

        final Instance instance = dataSet.get(0);

        Classifier c =  new KNearestNeighbors(5);
        c.buildClassifier(dataSet);

        Map<Object, Double> val = c.classDistribution(instance);



        System.out.println(instance);
        System.out.println(instance.classValue().toString());
        System.out.println(dataSet.size());
    }

    @Test
    public void getDataset() throws Exception {

    }

    @Test
    public void storeDataset() throws Exception {

    }

}