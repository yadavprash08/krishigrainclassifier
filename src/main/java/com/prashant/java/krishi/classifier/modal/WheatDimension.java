package com.prashant.java.krishi.classifier.modal;

import com.prashant.java.krishi.classifier.modal.wheat.InstanceTranslation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;
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

    private final static Field[] allFieldsInSortedOrder;

    private String immatureStatus;
    private String leftInStatus;

    static {
        final TreeMap<Integer, Field> fieldMap = new TreeMap<>();
        final List<Field> orderedFields = new ArrayList<>();
        Arrays.stream(WheatDimension.class.getDeclaredFields())
            .filter(f -> Objects.nonNull(f.getAnnotation(InstanceTranslation.class)))
            .forEachOrdered(f -> fieldMap.put(f.getAnnotation(InstanceTranslation.class).value(), f));
        fieldMap.forEach((k, v) -> orderedFields.add(v));
        allFieldsInSortedOrder = orderedFields.toArray(new Field[orderedFields.size()]);
    }

    public static WheatDimension createFromRow(String imageParticleRow) {
        final Double[] dimensions = Arrays.stream(StringUtils.split(imageParticleRow, "\t"))
            .map(Double::parseDouble)
            .collect(Collectors.toList())
            .toArray(new Double[] {});
        WheatDimension dimension = new WheatDimension();
        Arrays.stream(allFieldsInSortedOrder).forEachOrdered(f -> setFieldValue(f, dimensions, dimension));
        return dimension;
    }

    private static void setFieldValue(Field f, Double[] dimensions, WheatDimension dimension) {
        InstanceTranslation instanceTranslation = f.getAnnotation(InstanceTranslation.class);
        try {
            f.set(dimension, dimensions[instanceTranslation.value()]);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public Instance toImmatureDatasetInstance() {
        double[] instance = new double[allFieldsInSortedOrder.length];
        for (int i = 0; i < allFieldsInSortedOrder.length; i++) {
            final Double fieldDoubleValue = getFieldDoubleValue(allFieldsInSortedOrder[i]);
            instance[i] = fieldDoubleValue == null ? -1 : fieldDoubleValue.doubleValue();
        }
        DenseInstance denseInstance = new DenseInstance(instance, this.immatureStatus);
        return denseInstance;
    }

    private Double getFieldDoubleValue(Field field) {
        try {
            return (Double) field.get(this);
        } catch (IllegalAccessException e) {
            return null;
        }
    }

}
