package com.prashant.java.krishi.classifier.modal;

import com.prashant.java.krishi.classifier.modal.wheat.InstanceTranslation;
import lombok.*;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.SparseInstance;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter(AccessLevel.PRIVATE)
@EqualsAndHashCode
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WheatDimension {

    @InstanceTranslation(1)
    private Double area;
    @InstanceTranslation(2)
    private Double perimeter;
    @InstanceTranslation(3)
    private Double bx;
    @InstanceTranslation(4)
    private Double by;
    @InstanceTranslation(5)
    private Double width;
    @InstanceTranslation(6)
    private Double height;
    @InstanceTranslation(7)
    private Double major;
    @InstanceTranslation(8)
    private Double minor;
    @InstanceTranslation(9)
    private Double angle;
    @InstanceTranslation(10)
    private Double circumference;
    @InstanceTranslation(11)
    private Double feret;
    @InstanceTranslation(12)
    private Double feretx;
    @InstanceTranslation(13)
    private Double ferety;
    @InstanceTranslation(14)
    private Double feretAngle;
    @InstanceTranslation(15)
    private Double minFeret;
    @InstanceTranslation(16)
    private Double round;
    @InstanceTranslation(17)
    private Double solidity;

    private final static List<DimensionsMetadata> dimensionMetadatas = new ArrayList<>();

    private String immatureStatus;
    private String particleType;

    static {
        Arrays.stream(WheatDimension.class.getDeclaredFields())
                .filter(f -> Objects.nonNull(f.getAnnotation(InstanceTranslation.class)))
                .map(f -> new DimensionsMetadata(f))
                .forEachOrdered(dimensionMetadatas::add);
    }

    @AllArgsConstructor
    @Getter
    private static class DimensionsMetadata {
        private int instanceIndex;
        private Field field;

        public DimensionsMetadata(final Field f) {
            final InstanceTranslation instanceTranslation = f.getAnnotation(InstanceTranslation.class);
            this.instanceIndex = instanceTranslation.value();
            this.field = f;


        }

        public void setFieldValue(Double[] dimensions, WheatDimension dimension) {
            try {
                field.setAccessible(true);
                field.set(dimension, dimensions[instanceIndex]);
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException(e);
            }
        }

        public void updateInstance(WheatDimension wheatDimension, Instance instance) {
            Optional<Double> fieldDoubleValue = getFieldDoubleValue(wheatDimension);
            fieldDoubleValue.ifPresent(d -> instance.put(instanceIndex, d.doubleValue()));
        }

        private Optional<Double> getFieldDoubleValue(WheatDimension wheatDimension) {
            try {
                field.setAccessible(true);
                Double aDouble = (Double) field.get(wheatDimension);
                return Optional.ofNullable(aDouble);
            } catch (Exception e) {
                return Optional.empty();
            }
        }
    }

    public static WheatDimension createFromRow(String imageParticleRow) {
        final Double[] dimensions = Arrays.stream(StringUtils.split(imageParticleRow, "\t"))
                .map(Double::parseDouble)
                .collect(Collectors.toList())
                .toArray(new Double[]{});
        WheatDimension dimension = new WheatDimension();
        dimensionMetadatas.stream()
                .filter(f -> f.getInstanceIndex() < dimensions.length)
                .forEachOrdered(f -> f.setFieldValue(dimensions, dimension));
        return dimension;
    }


    public Instance immatureInstance() {
        Instance instance = sparceInstance();
        Optional.ofNullable(immatureStatus).ifPresent(instance::setClassValue);
        return instance;
    }

    public Instance foreignParticleStatus() {
        Instance instance = sparceInstance();
        Optional.ofNullable(particleType).ifPresent(instance::setClassValue);
        return instance;
    }

    private Instance sparceInstance() {
        Instance instance = new SparseInstance(dimensionMetadatas.size());
        dimensionMetadatas.stream().forEach(f -> f.updateInstance(this, instance));
        return instance;
    }
}
