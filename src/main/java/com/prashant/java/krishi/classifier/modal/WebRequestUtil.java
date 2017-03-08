package com.prashant.java.krishi.classifier.modal;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.context.request.WebRequest;

import java.util.Optional;

/**
 *
 */
@RequiredArgsConstructor
public class WebRequestUtil {
    @NonNull
    private final WebRequest request;

    public double doubleValue(@NonNull String key) {
        return Optional.ofNullable(request.getParameter(key)).map(this::mapToDouble).orElse(-1d);
    }

    private Double mapToDouble(String s) {
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException nfe) {
            return -1d;
        }
    }
}
