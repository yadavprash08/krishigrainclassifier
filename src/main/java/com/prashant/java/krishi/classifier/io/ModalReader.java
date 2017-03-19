package com.prashant.java.krishi.classifier.io;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.inject.Inject;
import com.prashant.java.krishi.classifier.modal.wheat.WheatDimension;
import lombok.NonNull;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 *
 */
public class ModalReader {
    private final Gson gson;
    private final Collection<WheatDimension> dimensions;

    @Inject
    public ModalReader(@NonNull Gson gson, @NonNull ModalInputSupplier modalInputSupplier) {
        this.gson = gson;
        this.dimensions = readAllWheatDimensions(modalInputSupplier);
    }

    private Collection<WheatDimension> readAllWheatDimensions(@NonNull ModalInputSupplier modalInputSupplier) {
        try {
            final Reader fileReader = modalInputSupplier.get();
            JsonReader reader = new JsonReader(fileReader);
            reader.setLenient(true);
            List<WheatDimension> dimensions = new ArrayList<>();
            while (!Objects.equals(reader.peek(), JsonToken.END_DOCUMENT)) {
                dimensions.add(gson.fromJson(reader, WheatDimension.class));
            }
            reader.close();
            return dimensions;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates the dataset instance for the immature classification.
     *
     * @return Dataset for all the attributes with proper class values for immature class.
     */
    public Dataset immatureDataset() {
        final Dataset dataset = new DefaultDataset();
        this.dimensions.parallelStream()
            .filter(w -> StringUtils.isNotBlank(w.getImmatureStatus()))
            .map(WheatDimension::immatureInstance)
            .forEach(dataset::add);
        return dataset;
    }

    /**
     * Creates the dataset instance for the immature classification.
     *
     * @return Dataset for all the attributes with proper class values for foreign class.
     */
    public Dataset foreignDataset() {
        final Dataset dataset = new DefaultDataset();
        this.dimensions.parallelStream()
            .filter(w -> StringUtils.isNotBlank(w.getParticleType()))
            .map(WheatDimension::particleTypeInstance)
            .forEach(dataset::add);
        return dataset;
    }

}
