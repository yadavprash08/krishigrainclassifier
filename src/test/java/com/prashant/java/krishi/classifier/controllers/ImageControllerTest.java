package com.prashant.java.krishi.classifier.controllers;

import com.prashant.java.krishi.classifier.modal.WheatDimension;
import ij.IJ;
import ij.ImagePlus;
import ij.measure.Measurements;
import ij.measure.ResultsTable;
import ij.plugin.filter.ParticleAnalyzer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.ImagingConstants;
import org.apache.commons.imaging.common.BufferedImageFactory;
import org.apache.commons.imaging.common.ImageMetadata;
import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 *
 */
@Slf4j
public class ImageControllerTest {
    private static final Map<String, Object> IMAGE_OPEN_PARAMS = new HashMap<>();
    public static class ManagedImageBufferedImageFactory implements
        BufferedImageFactory {

        @Override
        public BufferedImage getColorBufferedImage(final int width, final int height,
            final boolean hasAlpha) {
            final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            final GraphicsDevice gd = ge.getDefaultScreenDevice();
            final GraphicsConfiguration gc = gd.getDefaultConfiguration();
            return gc.createCompatibleImage(width, height,
                Transparency.TRANSLUCENT);
        }

        @Override
        public BufferedImage getGrayscaleBufferedImage(final int width, final int height,
            final boolean hasAlpha) {
            return getColorBufferedImage(width, height, hasAlpha);
        }
    }

    static {
        IMAGE_OPEN_PARAMS.put(ImagingConstants.BUFFERED_IMAGE_FACTORY, new ManagedImageBufferedImageFactory());
    }

    @Test
    public void readImage() throws Exception {
        File input = new File("/Users/yprasha/Downloads/gp/3.jpg");
        ImageMetadata metadata = Imaging.getMetadata(input);

        log.info("Metadata Items Size: {}",metadata.getItems().size());
        metadata.getItems()
            .stream()
            .forEach(this::processMetadata);

        ImagePlus img = IJ.openImage(input.getPath());
        IJ.run(img, "8-bit", "");
        IJ.run(img, "Make Binary", "");
        IJ.run(img, "Fill Holes", "");
        IJ.run(img, "Remove Outliers...", "radius=5 threshold=50 which=Dark stack");
        IJ.run(img, "Watershed", "input=blobs mask=None use min=0 max=150");

        int min = 0; //minimum particle size
        int max = 99999; //max particle size

        int options = ParticleAnalyzer.AREA + ParticleAnalyzer.ELLIPSE + ParticleAnalyzer.CIRCULARITY + ParticleAnalyzer.PERIMETER + ParticleAnalyzer.RECT + ParticleAnalyzer.FERET;
        int measurements = Measurements.AREA + Measurements.MEAN + Measurements.MIN_MAX + Measurements.STD_DEV + Measurements.MODE + Measurements.MEDIAN + Measurements.AREA_FRACTION + Measurements.LIMIT;

        final ResultsTable rt = new ResultsTable();//if you define a resultsTable yourself, you can explicitly call it
        // later

        ParticleAnalyzer pa = new ParticleAnalyzer(measurements, options, rt, min, max);

        pa.analyze(img);

        IntStream.range(0, rt.getCounter()-1)
            .mapToObj(i->rt.getRowAsString(i))
            .map(WheatDimension::createFromRow)
            .map(Objects::toString)
            .forEach(log::info);
    }

    private void processMetadata(ImageMetadata.ImageMetadataItem imageMetadataItem) {
        log.info(imageMetadataItem.toString());
    }
}