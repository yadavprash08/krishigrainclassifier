package com.prashant.java.krishi.classifier.imaging;

import com.prashant.java.krishi.classifier.modal.grain.GrainDimensions;

import java.io.File;
import java.util.Collection;

/**
 * Simple interface to abstract the image processing for the input image.
 */
public interface ImageProcessor {
    Collection<GrainDimensions> processImage(File imageFile);
}
