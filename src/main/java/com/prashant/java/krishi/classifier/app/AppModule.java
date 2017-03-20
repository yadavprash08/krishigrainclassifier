package com.prashant.java.krishi.classifier.app;

import com.google.gson.Gson;
import com.google.inject.AbstractModule;
import com.prashant.java.krishi.classifier.imaging.ImageJProcessor;
import com.prashant.java.krishi.classifier.imaging.ImageProcessor;
import com.prashant.java.krishi.classifier.io.ModalInputSupplier;
import com.prashant.java.krishi.classifier.ml.Classifiers;
import com.prashant.java.krishi.classifier.ml.KnnClassifiers;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * This is core module to handle all the injection in the application.
 */
@RequiredArgsConstructor
public class AppModule extends AbstractModule {
    @NonNull
    private final AppArguments appArguments;

    @Override
    protected void configure() {
        bind(AppArguments.class).toInstance(appArguments);
        bind(Gson.class).toInstance(new Gson());
        bind(ModalInputSupplier.class).toInstance(appArguments::getModalInputSupplier);
        bind(Classifiers.class).to(KnnClassifiers.class);
        bind(ImageProcessor.class).to(ImageJProcessor.class);
    }
}
