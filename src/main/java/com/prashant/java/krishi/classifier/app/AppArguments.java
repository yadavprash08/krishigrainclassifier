package com.prashant.java.krishi.classifier.app;

import lombok.Getter;
import org.kohsuke.args4j.Option;

import java.io.File;

/**
 *
 */
@Getter
public class AppArguments {

    @Option(name = "-sourceDataset", usage = "This is the dataset file which holds all the files pre-classified"
        + ".", required = true)
    private String sourceDatasetFileName;

    @Option(name = "-input", usage = "Image which should be classified.")
    private String processImage;

    @Option(name = "-help")
    private boolean help;

    public File getSourceFile() {
        return new File(sourceDatasetFileName);
    }
}
