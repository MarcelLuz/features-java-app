package de.sybit.mlz;

import de.sybit.mlz.extractors.graph.FeatureJavacExtractor;
import de.sybit.mlz.extractors.path.PathExtractor;
import de.sybit.mlz.utils.PropertiesLoader;
import de.sybit.mlz.utils.RepoAnalyser;
import org.apache.log4j.Logger;

import java.util.Properties;

public class FeatureApp {

    private final static Logger LOGGER = Logger.getLogger(FeatureApp.class.getName());
    private final static String FEATURE_JAVAC_EXTRACTOR_KEY = "feature-javac";
    private final static String PATH_EXTRACTOR_KEY = "path-extractor";
    private final static String SINGLE_FILE_MODE = "single-file";
    private final static String PATH_MODE = "path";

    public static void main(String[] args) {
        final Properties config = PropertiesLoader.loadProperties("app.properties");

        final String extractorType = config.getProperty("whichExtractor");
        final String extractMode = config.getProperty("extractMode");
        final int numThreads = Integer.valueOf(config.getProperty("numThreads"));

        final String pathToFile = config.getProperty("pathToFile");
        final String pathToRepo = config.getProperty("pathToRepo");

        final boolean dotOutput = Boolean.valueOf(config.getProperty("dotOutput"));
        final boolean verboseDot = Boolean.valueOf(config.getProperty("verboseDot"));
        final String protoDestinationPath = config.getProperty("protoDestinationPath");

        final int maxPathLength = Integer.valueOf(config.getProperty("maxPathLength"));
        final int maxPathWidth = Integer.valueOf(config.getProperty("maxPathWidth"));
        final int minCodeLen = Integer.valueOf(config.getProperty("minCodeLen"));
        final int maxCodeLen = Integer.valueOf(config.getProperty("maxCodeLen"));
        final int maxChildId = Integer.valueOf(config.getProperty("maxChildId"));
        final String datasetDestinationPath = config.getProperty("datasetDestinationPath");
        final String datasetName = config.getProperty("datasetName");

        switch (extractorType) {
            case FEATURE_JAVAC_EXTRACTOR_KEY: {
                FeatureJavacExtractor featureJavacExtractor = new FeatureJavacExtractor();
                if (extractMode.equals(SINGLE_FILE_MODE)) {
                    featureJavacExtractor.extractProtoFromSingleFile(pathToFile, protoDestinationPath, dotOutput, verboseDot);
                } else if (extractMode.equals(PATH_MODE)) {
                    LOGGER.info("Number of java files to process: " + RepoAnalyser.getNumberOfFilesInRepo(pathToRepo));
                    LOGGER.info("Start to process repo.");
                    featureJavacExtractor.extractProtoFromAllFilesInDirectory(pathToRepo, protoDestinationPath, dotOutput, verboseDot, numThreads);
                    LOGGER.info("Finished possessing");
                }
                break;
            }
            case PATH_EXTRACTOR_KEY: {
                PathExtractor pathExtractor = new PathExtractor(
                        maxPathLength,
                        maxPathWidth,
                        minCodeLen,
                        maxCodeLen,
                        maxChildId
                );
                if (extractMode.equals(SINGLE_FILE_MODE)) {
                    pathExtractor.extractPathsFromSingleFile(pathToFile, datasetDestinationPath);
                } else if (extractMode.equals(PATH_MODE)) {
                    LOGGER.info("Number of java files to process: " + RepoAnalyser.getNumberOfFilesInRepo(pathToRepo));
                    LOGGER.info("Start to process repo.");
                    pathExtractor.extractPathsFromAllFilesInDirectory(pathToRepo, datasetDestinationPath, datasetName, numThreads);
                    LOGGER.info("Finished possessing");
                }
                break;
            }
            default: {
                LOGGER.error("wrong extractor type: you can only use feature-javac or  path-extractor");
                break;
            }
        }
    }
}
