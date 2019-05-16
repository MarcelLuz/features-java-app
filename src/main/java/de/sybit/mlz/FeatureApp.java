package de.sybit.mlz;

import org.apache.log4j.Logger;

public class FeatureApp {

    private final static Logger LOGGER = Logger.getLogger(FeatureApp.class.getName());

    public static void main(String[] args) {
        String pathToJar = "D:\\Repos\\master-impl\\01_Data\\features-java-graph-app\\src\\main\\resources\\features-javac-1.0.0-SNAPSHOT-jar-with-dependencies.jar";
        String pathToFile = "C:\\Users\\mlz\\Downloads\\test\\original\\Test.java";
        String pathToRepo = "C:\\Users\\mlz\\Downloads\\test\\original";
        String destinationPath = "C:\\Users\\mlz\\Downloads\\test\\proto";
        boolean dotOutput = false;

        FeatureExtractor featureExtractor = new FeatureExtractor(pathToJar);

        featureExtractor.extractProtoFromSingleFile(pathToFile, destinationPath, dotOutput);

//        featureExtractor.extractProtoAllFilesInDirectory(pathToRepo, destinationPath, dotOutput);

    }


}
