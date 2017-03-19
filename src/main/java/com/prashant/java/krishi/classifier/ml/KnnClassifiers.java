package com.prashant.java.krishi.classifier.ml;

import com.google.inject.Inject;
import com.prashant.java.krishi.classifier.io.ModalReader;
import com.prashant.java.krishi.classifier.modal.wheat.WheatDimension;
import lombok.NonNull;
import net.sf.javaml.classification.Classifier;
import net.sf.javaml.classification.KNearestNeighbors;

import java.util.Objects;

/**
 * Basic classifier for all the different parameters.
 */
public class KnnClassifiers implements Classifiers {

    @NonNull
    private final Classifier immatureClassifier;

    @NonNull
    private final Classifier particleTypeClassifier;

    @Inject
    public KnnClassifiers(@NonNull ModalReader modalReader) {
        immatureClassifier = new KNearestNeighbors(5);
        immatureClassifier.buildClassifier(modalReader.immatureDataset());
        particleTypeClassifier = new KNearestNeighbors(5);
        particleTypeClassifier.buildClassifier(modalReader.foreignDataset());
    }

    @Override
    public WheatDimension classify(@NonNull final WheatDimension dimension){
        final String immatureState = Objects.toString(immatureClassifier.classify(dimension.immatureInstance()));
        final String particleType = Objects.toString(particleTypeClassifier.classify(dimension.particleTypeInstance()));
        return dimension.withImmatureStatus(immatureState).withParticleType(particleType);
    }
}
