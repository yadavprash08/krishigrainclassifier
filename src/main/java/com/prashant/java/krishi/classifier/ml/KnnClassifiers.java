package com.prashant.java.krishi.classifier.ml;

import com.google.inject.Inject;
import com.prashant.java.krishi.classifier.io.ModalReader;
import com.prashant.java.krishi.classifier.modal.grain.GrainDimensions;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.sf.javaml.classification.Classifier;
import net.sf.javaml.classification.KNearestNeighbors;

import java.util.Objects;

/**
 * Basic classifier for all the different parameters.
 */
@Slf4j
public class KnnClassifiers implements Classifiers {

    @NonNull
    private final Classifier grainTypeClassifier;

    @NonNull
    private final Classifier particleTypeClassifier;

    @Inject
    public KnnClassifiers(@NonNull ModalReader modalReader) {
        grainTypeClassifier = new KNearestNeighbors(2);
        grainTypeClassifier.buildClassifier(modalReader.immatureDataset());
        particleTypeClassifier = new KNearestNeighbors(2);
        particleTypeClassifier.buildClassifier(modalReader.foreignDataset());
    }

    @Override
    public GrainDimensions classify(@NonNull final GrainDimensions dimension){
        log.info("Classifying the image dimension: {}", dimension);
        final String grainType = Objects.toString(grainTypeClassifier.classify(dimension.grainTypeInstance()));
        final String particleType = Objects.toString(particleTypeClassifier.classify(dimension.particleTypeInstance()));
        return dimension.withGrainType(grainType).withParticleType(particleType);
    }
}
