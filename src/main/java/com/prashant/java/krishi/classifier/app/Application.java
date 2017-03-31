package com.prashant.java.krishi.classifier.app;

import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.prashant.java.krishi.classifier.imaging.ImageProcessor;
import com.prashant.java.krishi.classifier.modal.grain.GrainDimensions;
import com.prashant.java.krishi.classifier.modal.grain.type.GrainType;
import com.prashant.java.krishi.classifier.modal.grain.type.ParticleType;
import com.prashant.java.krishi.classifier.report.WheatAnalysisReport;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Arrays;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.io.OutputStreamWriter;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Path;
import java.text.MessageFormat;

import static java.nio.file.Files.newByteChannel;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * This is the root application to start the application. This will perform initializing the application.
 */
@Slf4j
public class Application {

    public static void main(String[] args) throws Exception {
        AppArguments arguments = new AppArguments();
        CmdLineParser parser = new CmdLineParser(arguments);
        if (Arrays.isNullOrEmpty(args)) {
            // Populate the app arguments.
            System.out.println("No Arguments provided.");
        }
        try {
            parser.parseArgument(args);
            final AppModule app = new AppModule(arguments);
            assertThat(app).isNotNull();
            final Injector injector = Guice.createInjector(app);
            assertThat(injector).isNotNull();
            if (arguments.isAddToStore()) {
                assertThat(arguments.getSourceDatasetFileName()).isNotEmpty();
                assertThat(arguments.getGrainType()).isNotEmpty();
                assertThat(arguments.getParticleType()).isNotEmpty();

                ParticleType particleType = ParticleType.fromString(arguments.getParticleType());
                GrainType grainType = GrainType.fromString(arguments.getGrainType());

                final Gson gson = injector.getInstance(Gson.class);

                final Path sourceDataSetPath = arguments.getSourceDataSetPath().get();
                final SeekableByteChannel byteChannel = newByteChannel(sourceDataSetPath, CREATE, APPEND);

                // All the required arguments are not null anymore so we can process the image now.
                log.info(
                    MessageFormat.format("Processing the image as {0} grains of type {1}", particleType, grainType));

                ImageProcessor processor = injector.getInstance(ImageProcessor.class);
                processor.processImage(arguments.getImageToProcess()).stream()
                    .map(a -> a.withParticleType(particleType.getStringValue()))
                    .map(a -> a.withGrainType(grainType.getStringValue())).map(Application::logGrainDimenstions)
                    .forEach(a -> a.writeToChannel(gson, byteChannel));
                byteChannel.close();

            } else {
                ImageAnalyzer imageAnalyzer = injector.getInstance(ImageAnalyzer.class);
                WheatAnalysisReport report = imageAnalyzer.processImage(arguments.getImageToProcess());
                OutputStreamWriter writer = new OutputStreamWriter(System.out);
                report.generateReport(writer);
                writer.flush();
            }
        } catch (CmdLineException e) {
            parser.printUsage(System.out);
            throw new RuntimeException("Invalid Arguments", e);
        }
    }

    public static GrainDimensions logGrainDimenstions(final GrainDimensions a) {
        log.info("Adding Image Particle: {}", a);
        return a;
    }

}
