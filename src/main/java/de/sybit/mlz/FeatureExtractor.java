package de.sybit.mlz;

import de.sybit.mlz.utils.FileExtractor;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;

public class FeatureExtractor {

    private final static Logger LOGGER = Logger.getLogger(FeatureExtractor.class.getName());
    private final static String INNER_QUOTE = "\"";
    private String jarPath;


    public FeatureExtractor(String jarPath) {
        this.jarPath = jarPath;
    }


    public void extractProtoFromSingleFile(String filePath, String destinationPath, boolean dotFile) {
        callExtractorJar(filePath, destinationPath, dotFile);
    }


    public void extractProtoAllFilesInDirectory(String projectPath, String destinationPath, boolean dotFile) {
        FileExtractor fileExtractor = new FileExtractor();
        List<String> allFilePaths = fileExtractor.getAllFilePaths(projectPath);
        allFilePaths.forEach(fp -> callExtractorJar(fp, destinationPath, dotFile));

    }

    private void callExtractorJar(String filePath, String destinationPath, boolean dotFile) {
        try {
            String[] command = {"javac", "-cp", jarPath, "-Xplugin:" + INNER_QUOTE + "FeaturePlugin",
                    destinationPath, dotFile + INNER_QUOTE, filePath};
            ProcessBuilder processBuilder = new ProcessBuilder().inheritIO().command(command);
            processBuilder.start();
        } catch (IOException ioException) {
            LOGGER.error("Error calling Extractor!", ioException);
        }
    }
}
