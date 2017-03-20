package com.prashant.java.krishi.classifier.controllers;

import com.google.gson.Gson;
import com.prashant.java.krishi.classifier.modal.wheat.WheatDimension;
import ij.IJ;
import ij.ImagePlus;
import ij.measure.Measurements;
import ij.measure.ResultsTable;
import ij.plugin.filter.ParticleAnalyzer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

/**
 *
 */
@Slf4j
@RunWith(Parameterized.class)
public class ImageControllerTest {

    private static final Gson GSON = new Gson();

    @Parameter(0)
    public File file;

    @Parameter(1)
    public String testName;

    @Parameters(name = "File:# {1}")
    public static Collection<Object[]> data() throws Exception {
        String path = Optional.ofNullable(System.getProperty("sourceImagesPath")).orElse(".");
        return Files.list(Paths.get(path))
            .filter(Files::isReadable)
            .filter(Files::isRegularFile)
            .map(Path::toFile)
            .filter(f -> StringUtils.endsWithIgnoreCase(f.getName(), ".jpg"))
            .map(f -> new Object[] { f, f.getName() })
            .collect(Collectors.toList());
    }

    @Test
    public void readImage() throws Exception {
        Assume.assumeTrue(StringUtils.equalsIgnoreCase(System.getProperty("createFile"), "dataset"));

        log.info("{}", file.exists());
        ImagePlus img = IJ.openImage(file.getPath());
        IJ.run(img, "8-bit", "");
        IJ.run(img, "Make Binary", "");
        IJ.run(img, "Fill Holes", "");
        IJ.run(img, "Remove Outliers...", "radius=5 threshold=50 which=Dark stack");
        IJ.run(img, "Watershed", "input=blobs mask=None use min=0 max=150");

        int min = 0; //minimum particle size
        int max = 99999; //max particle size

        int options =
            ParticleAnalyzer.AREA + ParticleAnalyzer.ELLIPSE + ParticleAnalyzer.CIRCULARITY + ParticleAnalyzer.PERIMETER
                + ParticleAnalyzer.RECT + ParticleAnalyzer.FERET;
        int measurements =
            Measurements.AREA + Measurements.MEAN + Measurements.MIN_MAX + Measurements.STD_DEV + Measurements.MODE
                + Measurements.MEDIAN + Measurements.AREA_FRACTION + Measurements.LIMIT;

        final ResultsTable rt = new ResultsTable();
        ParticleAnalyzer pa = new ParticleAnalyzer(measurements, options, rt, min, max);
        pa.analyze(img);
        final Path newFilePath = getPath();
        final SeekableByteChannel byteChannel = Files.newByteChannel(newFilePath, CREATE, APPEND);
        final ResultToDimensions rslt = new ResultToDimensions(rt, byteChannel);

        List<WheatDimension> dimensions = IntStream.range(0, rt.getCounter() - 1)
            .mapToObj(rslt::dimension)
            .map(rslt::writeToChannel)
            .collect(Collectors.toList());

        log.info("Content written to {}", newFilePath.toAbsolutePath().toString());
        byteChannel.close();

    }

    private Path getPath() {
        return Paths.get("basic_dataset.json").toAbsolutePath();
    }

    @RequiredArgsConstructor
    private class ResultToDimensions {
        private final ResultsTable resultsTable;
        private final SeekableByteChannel byteChannel;

        private WheatDimension dimension(int i) {
            final String rowAsString = resultsTable.getRowAsString(i);
            WheatDimension dimension = WheatDimension.createFromRow(rowAsString)
                .withFileParticleName("ID:#" + i)
                .withFilePath(file.getPath())
                .withFileName(file.getName());
            return new NativeClassifier(rowAsString, dimension).processWheatDimension();
        }

        private WheatDimension writeToChannel(WheatDimension dimension) {
            try {
                String jsonContent = GSON.toJson(dimension) + "\n";
                final ByteBuffer byteBuffer = ByteBuffer.wrap(jsonContent.getBytes());
                byteChannel.write(byteBuffer);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
            return dimension;
        }
    }

}