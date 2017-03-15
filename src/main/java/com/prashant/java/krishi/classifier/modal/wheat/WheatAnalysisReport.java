package com.prashant.java.krishi.classifier.modal.wheat;

import java.io.IOException;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 *
 */
public class WheatAnalysisReport implements Consumer<WheatDimension> {

    private final EnumMap<ParticleType, List<WheatDimension>> particleTypeMap = new EnumMap<>(ParticleType.class);
    private final EnumMap<ImmatureStatus, List<WheatDimension>> immatureMap = new EnumMap<>(ImmatureStatus.class);
    private final AtomicInteger totalParticles = new AtomicInteger(0);

    @Override
    public void accept(WheatDimension dimension) {
        totalParticles.incrementAndGet();
        particleTypeMap.computeIfAbsent(dimension.particleType(), (p) -> new ArrayList<>()).add(dimension);
        immatureMap.computeIfAbsent(dimension.immatureStatus(), (p) -> new ArrayList<>()).add(dimension);
    }

    public void generateReport(Writer writer) throws IOException {
        final int i = totalParticles.get();
        writer.write(MessageFormat.format("Total particles identified: #{0}\n", i));
        writer.write("Particle Type Analysis\n");
        final StringBuilder builder = new StringBuilder("\n");
        particleTypeMap.forEach((k, v) -> {
            final int size = v.size();
            builder.append(k.toString()).append(": ")
                .append(size).append("(")
                .append(((double) size / (double) i)).append(")")
                .append(":: ");
            v.forEach(builder.append(", ")::append);
            builder.append("\n");
        });
        writer.write(builder.toString());

        final StringBuilder immBuilder = new StringBuilder("\n");
        immatureMap.forEach((k, v) -> {
            final int size = v.size();
            immBuilder.append(k.toString()).append(": ")
                .append(size).append("(")
                .append(((double) size / (double) i)).append(")")
                .append(":: ");
            v.forEach(immBuilder.append(", ")::append);
            immBuilder.append("\n");
        });
        writer.write(immBuilder.toString());
    }
}
