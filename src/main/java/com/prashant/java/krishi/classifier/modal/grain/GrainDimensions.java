package com.prashant.java.krishi.classifier.modal.grain;

import com.google.gson.Gson;
import com.prashant.java.krishi.classifier.modal.grain.translation.InstanceTranslation;
import com.prashant.java.krishi.classifier.modal.grain.type.GrainType;
import com.prashant.java.krishi.classifier.modal.grain.type.ParticleType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Wither;
import lombok.extern.slf4j.Slf4j;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.SparseInstance;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter(AccessLevel.PRIVATE)
@EqualsAndHashCode
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class GrainDimensions {
    private final static List<DimensionsMetadata> DIMENSIONS_METADATA = new ArrayList<>();
    private static final String COMMA_DELIMIT = ",";
    private static final String CRLN = "\n";

    static {
        Arrays.stream(GrainDimensions.class.getDeclaredFields())
            .filter(f -> Objects.nonNull(f.getAnnotation(InstanceTranslation.class))).map(DimensionsMetadata::new)
            .forEachOrdered(DIMENSIONS_METADATA::add);
    }

    @Wither private String fileName;
    @Wither private String filePath;
    @Wither private String fileParticleName;
    @InstanceTranslation(1) private Double area;
    @InstanceTranslation(2) private Double perimeter;
    @InstanceTranslation(7) private Double major;
    @InstanceTranslation(8) private Double minor;
    @InstanceTranslation(17) private Double solidity;
    @Wither private String grainType;
    @Wither private String particleType;
    //    @InstanceTranslation(3) private Double bx;
    //    @InstanceTranslation(4) private Double by;
    //    @InstanceTranslation(5) private Double width;
    //    @InstanceTranslation(6) private Double height;
    //    @InstanceTranslation(9) private Double angle;
    //    @InstanceTranslation(10) private Double circumference;
    //    @InstanceTranslation(11) private Double feret;
    //    @InstanceTranslation(12) private Double feretx;
    //    @InstanceTranslation(13) private Double ferety;
    //    @InstanceTranslation(14) private Double feretAngle;
    //    @InstanceTranslation(15) private Double minFeret;
    //    @InstanceTranslation(16) private Double round;

    public static GrainDimensions createFromRow(String imageParticleRow) {
        final Double[] dimensions = Arrays.stream(StringUtils.split(imageParticleRow, "\t")).map(Double::parseDouble)
            .collect(Collectors.toList()).toArray(new Double[] {});
        GrainDimensions dimension = new GrainDimensions();
        DIMENSIONS_METADATA.stream().filter(f -> f.getInstanceIndex() < dimensions.length)
            .forEachOrdered(f -> f.setFieldValue(dimensions, dimension));
        return dimension;
    }

    public ParticleType particleType() {
        return ParticleType.fromString(this.particleType);
    }

    public GrainType grainType() {
        return GrainType.fromString(this.grainType);
    }

    public Instance grainTypeInstance() {
        Instance instance = sparseInstance();
        Optional.ofNullable(grainType).ifPresent(instance::setClassValue);
        return instance;
    }

    private Instance sparseInstance() {
        Instance instance = new SparseInstance(DIMENSIONS_METADATA.size());
        DIMENSIONS_METADATA.forEach(f -> f.updateInstance(this, instance));
        return instance;
    }

    public Instance particleTypeInstance() {
        Instance instance = sparseInstance();
        Optional.ofNullable(particleType).ifPresent(instance::setClassValue);
        return instance;
    }

    public GrainDimensions writeToChannel(final Gson gson, final SeekableByteChannel channel) {
        try {
            final byte[] bytes = StringUtils.join(gson.toJson(this), CRLN).getBytes();
            final ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
            channel.write(byteBuffer);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return this;
    }

    public static void writeToCsvHeaders(@NonNull final Writer writer) throws IOException {
        final StringBuilder headers = new StringBuilder("FileName,Particle,GrainType,ParticleTypes");
        DIMENSIONS_METADATA.stream().forEach(s -> headers.append(COMMA_DELIMIT).append(s.field.getName()));
        writer.write(headers.append(CRLN).toString());
    }

    public String toCSVString() {
        final StringBuilder sb = new StringBuilder(fileName).append(COMMA_DELIMIT).append(getFileParticleName())
            .append(COMMA_DELIMIT).append(grainType).append(COMMA_DELIMIT).append(particleType);
        DIMENSIONS_METADATA.stream()
            .forEach(s -> sb.append(",").append(s.getFieldDoubleValue(this).map(Objects::toString).orElse("")));
        return sb.append(CRLN).toString();
    }

    @AllArgsConstructor
    @Getter
    private static class DimensionsMetadata {
        private int instanceIndex;
        private Field field;

        DimensionsMetadata(final Field f) {
            final InstanceTranslation instanceTranslation = f.getAnnotation(InstanceTranslation.class);
            this.instanceIndex = instanceTranslation.value();
            this.field = f;
        }

        void setFieldValue(Double[] dimensions, GrainDimensions dimension) {
            try {
                field.setAccessible(true);
                field.set(dimension, dimensions[instanceIndex]);
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException(e);
            }
        }

        void updateInstance(GrainDimensions grainDimensions, Instance instance) {
            Optional<Double> fieldDoubleValue = getFieldDoubleValue(grainDimensions);
            fieldDoubleValue.ifPresent(d -> instance.put(instanceIndex, d));
        }

        private Optional<Double> getFieldDoubleValue(GrainDimensions grainDimensions) {
            try {
                field.setAccessible(true);
                Double aDouble = (Double) field.get(grainDimensions);
                return Optional.ofNullable(aDouble);
            } catch (Exception e) {
                return Optional.empty();
            }
        }
    }
}
