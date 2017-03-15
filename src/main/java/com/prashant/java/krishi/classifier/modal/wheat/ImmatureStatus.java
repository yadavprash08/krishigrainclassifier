package com.prashant.java.krishi.classifier.modal.wheat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 *
 */
@AllArgsConstructor
@Getter
public enum ImmatureStatus {

    HEALTHY_WHEAT("fine-wheat-grain"), BROKEN_WHEAT("broken-wheat-grain"), UNKNOWN("unknown");

    private String stringValue;

    public static ImmatureStatus fromString(String value) {
        return Arrays
            .stream(values())
            .filter(v -> StringUtils.equalsIgnoreCase(v.stringValue, value))
            .findFirst()
            .orElse(UNKNOWN);
    }
}
