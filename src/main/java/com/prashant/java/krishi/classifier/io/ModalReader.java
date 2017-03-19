package com.prashant.java.krishi.classifier.io;

import com.google.gson.Gson;
import lombok.Builder;

import java.io.File;

/**
 *
 */
@Builder
public class ModalReader {
    private final Gson gson;
    private final File inputFile;
}
