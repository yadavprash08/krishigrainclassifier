package com.prashant.java.krishi.classifier.ml;

import com.google.inject.Inject;
import com.prashant.java.krishi.classifier.io.ModalReader;
import com.prashant.java.krishi.classifier.modal.grain.GrainDimensions;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import net.sf.javaml.classification.Classifier;
import net.sf.javaml.classification.KNearestNeighbors;

import java.util.Objects;

/**
 * Basic classifier for all the different parameters.
 */
@Slf4j
@ToString
@EqualsAndHashCode
public class KnnClassifiers implements Classifiers {

    /**
     * This is the constant to define the value for looking the neighbour in the existing dataset.
     */
    private static final int K = 1;

    @NonNull private final Classifier grainTypeClassifier;

    @NonNull private final Classifier particleTypeClassifier;

    @Inject
    public KnnClassifiers(@NonNull ModalReader modalReader) {
        log.info("Creating the classifier using the modalReader: {}", modalReader);
        grainTypeClassifier = new KNearestNeighbors(K);
        grainTypeClassifier.buildClassifier(modalReader.grainTypeDataset());
        particleTypeClassifier = new KNearestNeighbors(K);
        particleTypeClassifier.buildClassifier(modalReader.particleTypeDataset());
    }

    @Override
    public GrainDimensions classify(@NonNull final GrainDimensions dimension) {
        log.info("Classifying the image dimension: {}", dimension);
        final String grainType = Objects.toString(grainTypeClassifier.classify(dimension.grainTypeInstance()));
        final String particleType = Objects.toString(particleTypeClassifier.classify(dimension.particleTypeInstance()));
        log.info("{} grain is classified as {} grain type with particle type {}", dimension, grainType, particleType);
        return dimension.withGrainType(grainType).withParticleType(particleType);
    }
}
