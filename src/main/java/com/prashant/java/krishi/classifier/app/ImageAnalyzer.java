package com.prashant.java.krishi.classifier.app;

import com.google.inject.Inject;
import com.prashant.java.krishi.classifier.imaging.ImageProcessor;
import com.prashant.java.krishi.classifier.ml.Classifiers;
import com.prashant.java.krishi.classifier.modal.wheat.WheatAnalysisReport;
import lombok.NonNull;

import java.io.File;

public class ImageAnalyzer {

    private final ImageProcessor imageProcessor;
    private final Classifiers classifiers;

    @Inject
    public ImageAnalyzer(ImageProcessor imageProcessor, Classifiers classifiers) {
        this.imageProcessor = imageProcessor;
        this.classifiers = classifiers;
    }

    public WheatAnalysisReport processImage(@NonNull File imageFile) {
        WheatAnalysisReport report = new WheatAnalysisReport();
        imageProcessor.processImage(imageFile).parallelStream()
            .map(classifiers::classify)
            .forEach(report);
        return report;
    }

}
