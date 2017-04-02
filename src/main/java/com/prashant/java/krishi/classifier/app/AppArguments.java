package com.prashant.java.krishi.classifier.app;

import lombok.Getter;
import org.kohsuke.args4j.Option;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

/**
 *
 */
@Getter
public class AppArguments {

    private static final String SRC_DATASET_USAGE = "This is the dataset file which holds all the files pre-classified.";
    @Option(name = "-sourceDataset", usage = SRC_DATASET_USAGE) private String sourceDatasetFileName;

    @Option(name = "-input", usage = "Image which should be classified.") private String processImage;

    @Option(name = "-addToSource", usage = "Adds the image to the source data store for further classification") private boolean addToStore;

    @Option(name = "-grainType", usage = "This is to state all the grain types in the image. All the grains in the "
        + "image are classified using the same type only.") private String grainType;

    @Option(name = "-particleType", usage = "To tell the application for all the grain types like wheat.") private String particleType;

    @Option(name = "-outputFile", usage = "Name for the output file to write the data into.") private String outputFile;

    @Option(name = "-dumpJson", usage = "Dump the data in json format into a javascript. This option will only work "
        + "with outputFile") private boolean dumpJson;

    @Option(name = "-help") private boolean help;

    public Reader getModalInputSupplier() {
        final Optional<Path> sourceFile = getSourceDataSetPath();
        try {
            if (sourceFile.isPresent()) {
                return Files.newBufferedReader(sourceFile.get());
            }
            InputStreamReader inputStreamReader = new InputStreamReader(
                AppArguments.class.getResourceAsStream("/basic_dataset.json"));
            return new BufferedReader(inputStreamReader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Path> getSourceDataSetPath() {
        return Optional.ofNullable(sourceDatasetFileName).map(File::new).map(File::toPath);
    }

    public File getImageToProcess() {
        return new File(processImage);
    }

    /**
     * Defines the output writer for the report of the analysis.
     *
     * @return Output {@link Writer} for the report.
     */
    public Writer outputWriter() {
        if (Objects.isNull(outputFile)) {
            return new OutputStreamWriter(System.out);
        }
        try {
            return Files.newBufferedWriter(Paths.get(outputFile), CREATE, TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //        throw new IllegalArgumentException("Unable to define the output pattern.");
    }

    /**
     * This is the json writer which can write the json data in java script consumable format.
     *
     * @return Json {@link Writer}
     */
    public Writer jsonWriter() throws IOException {
        Objects.requireNonNull(outputFile);
        return Files.newBufferedWriter(Paths.get(outputFile + ".js"), CREATE, TRUNCATE_EXISTING);
    }
}
