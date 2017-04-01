package com.prashant.java.krishi.classifier.imaging;

import com.prashant.java.krishi.classifier.modal.grain.GrainDimensions;
import ij.IJ;
import ij.ImagePlus;
import ij.measure.Measurements;
import ij.measure.ResultsTable;
import ij.plugin.filter.ParticleAnalyzer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 */
@Slf4j
public class ImageJProcessor implements ImageProcessor {

    private static final int options =
        ParticleAnalyzer.AREA + ParticleAnalyzer.ELLIPSE + ParticleAnalyzer.CIRCULARITY + ParticleAnalyzer.PERIMETER
            + ParticleAnalyzer.RECT + ParticleAnalyzer.FERET;
    private static final int measurements =
        Measurements.AREA + Measurements.MEAN + Measurements.MIN_MAX + Measurements.STD_DEV + Measurements.MODE
            + Measurements.MEDIAN + Measurements.AREA_FRACTION + Measurements.LIMIT;

    @Override
    public Collection<GrainDimensions> processImage(@NonNull File imageFile) {

        assertThat(imageFile.exists()).isTrue();

        ImagePlus img = IJ.openImage(imageFile.getPath());
        IJ.run(img, "8-bit", "");
        IJ.run(img, "Make Binary", "");
        IJ.run(img, "Fill Holes", "");
        IJ.run(img, "Remove Outliers...", "radius=5 threshold=50 which=Dark stack");
        IJ.run(img, "Watershed", "input=blobs mask=None use min=0 max=150");

        final int min = 0; //minimum particle size
        final int max = 99999; //max particle size

        final ResultsTable resultTable = new ResultsTable();
        ParticleAnalyzer pa = new ParticleAnalyzer(measurements, options, resultTable, min, max);
        //pa.setHideOutputImage(true);
        pa.analyze(img);
        img.close();

        final ResultToDimensions result = new ResultToDimensions(resultTable, imageFile);

        return IntStream.range(0, resultTable.getCounter()).mapToObj(result::dimension).filter(d -> d.getArea() >= 400)
            .collect(Collectors.toList());
    }

    @RequiredArgsConstructor
    private class ResultToDimensions {
        private final ResultsTable resultsTable;
        private final File file;

        private GrainDimensions dimension(int i) {
            log.info("Processing element {}", i);
            GrainDimensions dimension = GrainDimensions.createFromRow(resultsTable.getRowAsString(i));
            return dimension.withFileParticleName("ID:#" + (i + 1)).withFilePath(file.getPath())
                .withFileName(file.getName());
        }

    }
}
