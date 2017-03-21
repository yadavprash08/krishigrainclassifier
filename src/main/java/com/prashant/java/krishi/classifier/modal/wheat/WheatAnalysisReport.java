package com.prashant.java.krishi.classifier.modal.wheat;

import java.io.IOException;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 *
 */
public class WheatAnalysisReport implements Consumer<WheatDimension> {

    private final EnumMap<ParticleType, List<WheatDimension>> particleTypeMap = new EnumMap<>(ParticleType.class);
    private final AtomicInteger totalParticles = new AtomicInteger(0);

    @Override
    public void accept(WheatDimension dimension) {
        totalParticles.incrementAndGet();
        particleTypeMap.computeIfAbsent(dimension.particleType(), (p) -> new ArrayList<>()).add(dimension);
    }

    public void generateReport(Writer writer) throws IOException {
        final int i = totalParticles.get();
        writer.write(MessageFormat.format("Total particles identified: #{0}\n\n", i));
        writer.write("Particle Type Analysis\n");
        final StringBuilder builder = new StringBuilder();
        particleTypeMap.forEach((k, v) -> {
            final int size = v.size();
            builder.append(k.toString()).append(": ")
                .append(size).append("(")
                .append(((double) size / (double) i)).append(")")
                .append(":: ");
            v.stream().map(this::dimensionToString).forEach(builder::append);
            builder.append("\n");
        });
        writer.write(builder.toString());
    }

    private String dimensionToString(WheatDimension dimension) {
        return Optional.ofNullable(dimension).map(WheatDimension::getFileParticleName).orElse("_____") + ", ";
    }
}
