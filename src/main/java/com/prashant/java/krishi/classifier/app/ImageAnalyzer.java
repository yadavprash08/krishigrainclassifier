package com.prashant.java.krishi.classifier.app;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.prashant.java.krishi.classifier.imaging.ImageProcessor;
import com.prashant.java.krishi.classifier.ml.Classifiers;
import com.prashant.java.krishi.classifier.report.WheatAnalysisReport;
import lombok.NonNull;

import java.io.File;

public class ImageAnalyzer {

    @NonNull private final ImageProcessor imageProcessor;
    @NonNull private final Classifiers classifiers;
    @NonNull private final Gson gson;

    @Inject
    public ImageAnalyzer(ImageProcessor imageProcessor, Classifiers classifiers, Gson gson) {
        this.imageProcessor = imageProcessor;
        this.classifiers = classifiers;
        this.gson = gson;
    }

    public WheatAnalysisReport processImage(@NonNull File imageFile) {
        WheatAnalysisReport report = new WheatAnalysisReport(gson);
        imageProcessor.processImage(imageFile)
            .parallelStream()
            .map(classifiers::classify)
            .forEach(report);
        return report;
    }

}
