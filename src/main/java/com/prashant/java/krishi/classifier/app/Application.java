package com.prashant.java.krishi.classifier.app;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.prashant.java.krishi.classifier.modal.wheat.WheatAnalysisReport;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.io.File;
import java.io.OutputStreamWriter;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This is the root application to start the application. This will perform initializing the application.
 */
public class Application {

    public static void main(String[] args) throws Exception {
        AppArguments arguments = new AppArguments();
        CmdLineParser parser = new CmdLineParser(arguments);
        try {
            parser.parseArgument(args);
            final AppModule app = new AppModule(arguments);
            assertThat(app).isNotNull();
            final Injector injector = Guice.createInjector(app);
            assertThat(injector).isNotNull();

            ImageAnalyzer imageAnalyzer = injector.getInstance(ImageAnalyzer.class);
            WheatAnalysisReport report = imageAnalyzer.processImage(arguments.getImageToProcess());
            OutputStreamWriter writer = new OutputStreamWriter(System.out);
            report.generateReport(writer);
            writer.flush();
        } catch (CmdLineException e) {
            parser.printUsage(System.out);
            throw new RuntimeException("Invalid Arguments", e);
        }
    }

}
