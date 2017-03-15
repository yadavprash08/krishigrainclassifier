package com.prashant.java.krishi.classifier.app;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

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

        } catch (CmdLineException e) {
            parser.printUsage(System.out);
            throw new RuntimeException("Invalid Arguments", e);
        }
    }

}
