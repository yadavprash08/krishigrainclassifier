package com.prashant.java.krishi.classifier.repository;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.tools.data.FileHandler;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

@Component
public final class DataSetFactory {

    public static final String DATASET_SRC_FILE = "/tmp/krishiml/dataset.csv";

    private Dataset dataset;

    public static String createFile() throws IOException {
        String status = "FILE_EXISTED";
        final Path path = Paths.get(DATASET_SRC_FILE);
        final File file = new File(DATASET_SRC_FILE);
        final FileAttribute<Set<PosixFilePermission>> attributes = PosixFilePermissions
            .asFileAttribute(PosixFilePermissions.fromString("rwxr-xr-x"));
        final File parent = file.getParentFile();
        if (!Files.exists(path)) {
            if(!parent.exists() || !parent.isDirectory()){
                Files.createDirectory(parent.toPath(), attributes);
            }
            Files.createFile(path, attributes);
            status = "FILE_CREATED";
        }
        return status;
    }

    public void loadDataSet() throws IOException {
        this.dataset = FileHandler.loadDataset(new File(DATASET_SRC_FILE), 18, ",");
    }

    public Dataset getDataset() {
        return dataset;
    }

    public void storeDataset() throws IOException {
        FileHandler.exportDataset(dataset, new File(DATASET_SRC_FILE));
    }

}
