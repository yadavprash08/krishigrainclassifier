package com.prashant.java.krishi.classifier.report;

import com.google.gson.Gson;
import com.prashant.java.krishi.classifier.modal.grain.GrainDimensions;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This is the class to generate the reports for the classifications done in the program.
 */
@Slf4j
public class WheatAnalysisReport implements Consumer<GrainDimensions> {

    @NonNull private final Gson gson;

    private static final String COMMA_DELIMIT = ",";
    private static final String CR_LN = "\n";
    private final List<GrainDimensions> allGrains = new ArrayList<>();
    private final AtomicInteger totalParticles = new AtomicInteger(0);

    @Inject
    public WheatAnalysisReport(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void accept(GrainDimensions dimension) {
        System.out.println(dimension);
        totalParticles.incrementAndGet();
        allGrains.add(dimension);
    }

    public void generateReport(Writer writer) throws IOException {
        log.info("Adding the header for the csv file using writer {}.", writer);
        GrainDimensions.writeToCsvHeaders(writer);
        final int totalProcessedParticles = totalParticles.get();
        log.info("Total analysed particles: {}", totalProcessedParticles);
        for (GrainDimensions g : allGrains) {
            writer.write(g.toCSVString());
        }

        final String newLineGap = IntStream.range(0, 10).mapToObj(i -> "\n").collect(Collectors.joining());

        writer.write(newLineGap);
        writer.write("Particle Type Analysis\n");

        writeCollectiveStat(writer, w -> w.particleType().toString());

        writer.write(newLineGap);
        writer.write("Grain Type Analysis\n");

        writeCollectiveStat(writer, w -> w.grainType().toString());

        writer.flush();
    }

    public void writeCollectiveStat(Writer writer, Function<GrainDimensions, String> getParticleType)
        throws IOException {
        final Map<String, List<GrainDimensions>> particleTypeMap = allGrains.parallelStream()
            .collect(Collectors.groupingBy(getParticleType));
        String particleTypeAnalysis = dumpMapStats(particleTypeMap);
        writer.write(particleTypeAnalysis);
    }

    private String dumpMapStats(Map<String, List<GrainDimensions>> particleTypeMap) {
        final int totalProcessedParticles = totalParticles.get();
        final StringBuilder header = new StringBuilder("TotalParticles");
        final StringBuilder count = new StringBuilder("#" + totalProcessedParticles);
        final StringBuilder percentage = new StringBuilder("-");
        particleTypeMap.forEach((k, v) -> {
            final int size = v.size();
            final double p = ((double) totalProcessedParticles) / ((double) size) * 100;
            header.append(COMMA_DELIMIT).append(Objects.toString(k));
            count.append(COMMA_DELIMIT).append(size);
            percentage.append(COMMA_DELIMIT).append(String.format("%.2f", p)).append("%");
        });

        return header.toString() + CR_LN + count.toString() + CR_LN + percentage.toString() + CR_LN;
    }

    public void dumpJsFile(Writer jsonWriter) throws IOException {
        jsonWriter.write("var wg=[");
        String jsData = allGrains.stream().map(gson::toJson).collect(Collectors.joining(",\n"));
        jsonWriter.write(jsData);
        jsonWriter.write("];\n\n");
        jsonWriter.flush();
        jsonWriter.close();
    }
}
