package eu.mattflix;

import eu.mattflix.utils.SubFinder;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class App {
    public static Logger LOG = LoggerFactory.getLogger(App.class);
    private static final String APP_NAME = "SubComparer";
    private static final String APP_VERSION = "0.0.1";

    private static String destinationPath = "";
    private static String sourcePath = "";


    public static void main(String[] args) throws Exception {

        Options options = createOptions();
        double tolerance = -1;
        CommandLineParser parser = new DefaultParser();

        boolean hasRequired = parse(options, args);

        File originalFile = new File(sourcePath);
        File destinationFolder = new File(destinationPath);

        // Check parameters


        if (hasRequired && originalFile != null && destinationFolder != null) {

            CommandLine line = parser.parse(options, args);
            if (line.hasOption('t')) {
                LOG.info("Set tolerance to {}",line.getOptionValue('t'));
                tolerance = Double.valueOf(line.getOptionValue('t'));
            }

                LOG.info("Start to compare Original file '{}' with all files in folder '{}'...",originalFile.getAbsolutePath(),destinationFolder.getAbsolutePath());
                SubFinder sFinder = new SubFinder(originalFile, destinationFolder);
                sFinder.compare(tolerance);

                if (sFinder.getBestResult() != null) {
                    ///System.out.println("Best result is " + sFinder.getBestResult().getFileName() + " - Score : " + sFinder.getBestResult().getMatchRatio() + "%");
                    System.out.println(sFinder.getJSONBestResult());
                }

        } else {
            throw new ParseException("Missing parameters");
        }


    }



    private static Options createOptions() {

        // create Options object
        Options options = new Options();
        options.addOption("v", "version of the application");

        // Source file
        Option sourceOpt = Option.builder("s")
                .argName("source path")
                .hasArg()
                .desc("path of the source file")
                .required(true)
                .build();
        options.addOption(sourceOpt);

        // Destination Folder
        Option destFoldOpt = Option.builder("d")
                .argName("destination folder path")
                .hasArg(true)
                .desc("destination folder path to compare with source file")
                .required(true)
                .build();
        options.addOption(destFoldOpt);

        // Tolerance
        Option toleranceStartOpt = Option.builder("t")
                .argName("tolerance")
                .hasArg(true)
                .desc("tolerance between start position from source & comparison file")
                .required(false)
                .build();
        options.addOption(toleranceStartOpt);

        return options;
    }


    private static HelpFormatter createFormatter() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(150);


        return formatter;
    }

    private static boolean parse(Options options, String[] args) {
        CommandLineParser parser = new DefaultParser();
        boolean hasRequired = false;
        try {
            // parse the command line arguments
            CommandLine line = parser.parse(options, args);
            if (line.hasOption("v")) {
                // initialise the member variable
                System.out.println(APP_VERSION);
            }

            if (line.hasOption("d")) {
                destinationPath = line.getOptionValue("d");
                hasRequired = true;
            } else {
                hasRequired = false;
            }
            if (line.hasOption("s")) {
                sourcePath = line.getOptionValue("s");
                hasRequired = true;
            } else {
                hasRequired = false;
            }

            if (!hasRequired) {
                // /print help
                HelpFormatter helpFormatter = createFormatter();
                helpFormatter.printHelp(APP_NAME, options);
            }
            return hasRequired;
        } catch (ParseException exp) {
            // oops, something went wrong
            System.err.println("Parsing failed.  Reason: " + exp.getMessage());
            HelpFormatter helpFormatter = createFormatter();
            helpFormatter.printHelp(APP_NAME, options);
            return false;
        }
    }


}
