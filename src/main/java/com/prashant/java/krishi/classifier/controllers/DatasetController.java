package com.prashant.java.krishi.classifier.controllers;

import com.prashant.java.krishi.classifier.repository.DataSetFactory;
import net.sf.javaml.core.Dataset;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Objects;

/**
 *
 */
@RestController
public class DatasetController {

    @Resource
    private DataSetFactory dataSetFactory;

    @RequestMapping("/dataset/fileStatus")
    public String datasetFileStatus() throws IOException {
        return DataSetFactory.createFile();
    }

    @RequestMapping("/dataset/load")
    public String loadDataSet() throws IOException {
        dataSetFactory.loadDataSet();
        final Dataset dataset = dataSetFactory.getDataset();
        return Objects.toString(dataset.size());
    }


}
