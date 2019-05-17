package de.sybit.mlz;

import de.sybit.mlz.extractors.impl.FeatureJavacExtractor;
import de.sybit.mlz.extractors.impl.PathExtractor;
import de.sybit.mlz.utils.PropertiesLoader;
import org.apache.log4j.Logger;

import java.util.Properties;

public class FeatureApp {

    private final static Logger LOGGER = Logger.getLogger(FeatureApp.class.getName());
    private final static String FEATURE_JAVAC_EXTRACTOR_KEY = "feature-javac";
    private final static String PATH_EXTRACTOR_KEY = "path-extractor";

    public static void main(String[] args) {
        final Properties config = PropertiesLoader.loadProperties("app.properties");

        final String extractorType = config.getProperty("whichExtractor");

        final String pathToFeaturesJavacJar = config.getProperty("pathToFeaturesJavacJar");
        final String pathToPathExtractorJar = config.getProperty("pathToPathExtractorJar");

        final String pathToFile = config.getProperty("pathToFile");
        final String pathToRepo = config.getProperty("pathToRepo");

        final String destinationPath = config.getProperty("destinationPath");
        final boolean dotOutput = Boolean.valueOf(config.getProperty("dotOutput"));
        final boolean commandLineOutput = Boolean.valueOf(config.getProperty("commandLineOutput"));

        switch (extractorType) {
            case FEATURE_JAVAC_EXTRACTOR_KEY: {
                FeatureJavacExtractor featureJavacExtractor = new FeatureJavacExtractor(pathToFeaturesJavacJar);
                featureJavacExtractor.extractProtoFromSingleFile(pathToFile, destinationPath, dotOutput, commandLineOutput);
                featureJavacExtractor.extractProtoAllFilesInDirectory(pathToRepo, destinationPath, dotOutput, commandLineOutput);
                break;
            }
            case PATH_EXTRACTOR_KEY: {
                PathExtractor pathExtractor = new PathExtractor(pathToPathExtractorJar);
                break;
            }
            default: {
                LOGGER.error("Wrong Extractor Type: You can only use feature-javac or  path-extractor");
                break;
            }
        }
    }
}
