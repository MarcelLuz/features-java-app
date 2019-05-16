package de.sybit.mlz;

import de.sybit.mlz.utils.FileExtractor;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;

public class FeatureExtractor {

    private final static Logger LOGGER = Logger.getLogger(FeatureExtractor.class.getName());
    private String jarName;


    public FeatureExtractor(String jarName) {
        this.jarName = jarName;

    }


    public void extractProtoFromSingleFile(String filePath, String destinationPath, boolean dotFile) {
        callExtractorJar(filePath, destinationPath, dotFile);
    }


    public void extractProtoAllFilesInDirectory(String projectPath, String destinationPath, boolean dotFile) {
        FileExtractor fileExtractor = new FileExtractor();
        List<String> allFilePaths = fileExtractor.getAllFilePaths(projectPath);
        allFilePaths.forEach(fp -> callExtractorJar(fp, destinationPath, dotFile));

    }

    // Example: javac -cp .\features-javac-1.0.0-SNAPSHOT-jar-with-dependencies.jar -Xplugin:"FeaturePlugin D:\Repos\master-imp\01_Data\features-java-graph-app\src\main\resources\proto false" .\Test.java
    private void callExtractorJar(String filePath, String destinationPath, boolean dotFile) {
        try {
            String featurePluginCall = "-Xplugin:\"FeaturePlugin"  + destinationPath + " " + dotFile + "\"";

            String[] command = {"javac", "-cp", jarName, featurePluginCall, filePath};
            ProcessBuilder processBuilder = new ProcessBuilder().inheritIO().command(command);
            processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
