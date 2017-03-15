package com.prashant.java.krishi.classifier.ml;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.prashant.java.krishi.classifier.app.AppArguments;
import com.prashant.java.krishi.classifier.app.AppModule;
import com.prashant.java.krishi.classifier.modal.wheat.ImmatureStatus;
import com.prashant.java.krishi.classifier.modal.wheat.ParticleType;
import com.prashant.java.krishi.classifier.modal.wheat.WheatDimension;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 *
 */
@RunWith(Parameterized.class)
public class KnnClassifiersTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() throws Exception {
        AppArguments appArguments = new AppArguments();
        AppModule module = new AppModule(appArguments);
        final Injector injector = Guice.createInjector(module);

        InputStreamReader iReader = new InputStreamReader(KnnClassifiersTest.class.getResourceAsStream
            ("/test_classifier.json"));
        JsonReader reader = new JsonReader(iReader);
        reader.setLenient(true);
        final Gson gson = injector.getInstance(Gson.class);
        final Classifiers classifiers = injector.getInstance(Classifiers.class);

        List<WheatDimension> dimensions = new ArrayList<>();
        while (!Objects.equals(reader.peek(), JsonToken.END_DOCUMENT)) {
            dimensions.add(gson.fromJson(reader, WheatDimension.class));
        }
        return dimensions.stream()
            .map(d->mapToObjects(d, classifiers, gson))
            .collect(Collectors.toList());
    }

    private static Object[] mapToObjects(final WheatDimension d, final Classifiers classifiers, Gson gson) {
        WheatDimension input = d.withImmatureStatus(null).withParticleType(null);
        return new Object[] { input, d.immatureStatus(), d.particleType(), classifiers, gson };
    }

    @Parameter(0)
    public WheatDimension dimension;
    @Parameter(1)
    public ImmatureStatus expectedImmatureStatus;
    @Parameter(2)
    public ParticleType expectedParticleType;
    @Parameter(3)
    public Classifiers classifiers;
    @Parameter(4)
    public Gson gson;


    @Test
    public void classify() throws Exception {
        WheatDimension actual = classifiers.classify(dimension);
        System.out.println(gson.toJson(dimension));
        System.out.println(gson.toJson(actual));
        assertEquals(expectedImmatureStatus, actual.immatureStatus());
        assertEquals(expectedParticleType, actual.particleType());
    }

}