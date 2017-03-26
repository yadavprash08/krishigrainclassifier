package com.prashant.java.krishi.classifier.report;

import com.prashant.java.krishi.classifier.modal.grain.GrainDimensions;
import com.prashant.java.krishi.classifier.modal.grain.type.GrainType;
import com.prashant.java.krishi.classifier.modal.grain.type.ParticleType;

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
public class WheatAnalysisReport implements Consumer<GrainDimensions> {

    private final EnumMap<GrainType, List<GrainDimensions>> grainTypeMap = new EnumMap<>(GrainType.class);
    private final AtomicInteger totalParticles = new AtomicInteger(0);

    @Override
    public void accept(GrainDimensions dimension) {
        System.out.println(dimension);
        totalParticles.incrementAndGet();
        grainTypeMap.computeIfAbsent(dimension.grainType(), (p) -> new ArrayList<>()).add(dimension);
    }

    public void generateReport(Writer writer) throws IOException {
        final int i = totalParticles.get();
        writer.write(MessageFormat.format("Total particles identified: #{0}\n\n", i));
        writer.write("Particle Type Analysis\n");
        final StringBuilder builder = new StringBuilder();
        grainTypeMap.forEach((k, v) -> {
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

    private String dimensionToString(GrainDimensions dimension) {
        return Optional.ofNullable(dimension).map(GrainDimensions::getFileParticleName).orElse("_____") + ", ";
    }
}
