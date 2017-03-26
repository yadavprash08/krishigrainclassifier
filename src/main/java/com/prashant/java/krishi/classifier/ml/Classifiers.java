package com.prashant.java.krishi.classifier.ml;

import com.prashant.java.krishi.classifier.modal.grain.GrainDimensions;

/**
 * This is the basic interface which defines the contract on how would the classifiers respond with the results of
 * the classifications.
 */
public interface Classifiers {
    /**
     * Classifies the two attributes for the wheat dimensions and produce the next dimension with populated class
     * values.
     *
     * @param dimension {@link GrainDimensions} which represents the attribute of the wheat grain image.
     * @return WheatDimension with classified values.
     */
    GrainDimensions classify(GrainDimensions dimension);
}
