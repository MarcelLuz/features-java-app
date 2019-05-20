package de.sybit.mlz.extractors.impl;

import de.sybit.mlz.extractors.Extractor;
import de.sybit.mlz.utils.FileExtractor;
import org.apache.log4j.Logger;
import uk.ac.cam.acr31.features.javac.FeaturePlugin;
import uk.ac.cam.acr31.features.javac.graph.FeatureGraph;
import uk.ac.cam.acr31.features.javac.testing.TestCompilation;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FeatureJavacExtractor implements Extractor {

    private final static Logger LOGGER = Logger.getLogger(FeatureJavacExtractor.class.getName());
    private final static String INNER_QUOTE = "\"";
    private String jarPath;


    public FeatureJavacExtractor(String jarPath) {
        this.jarPath = jarPath;
    }


    @Override
    public void extractProtoFromSingleFile(String filePath, String destinationPath, boolean dotFile, boolean commandLineOutput) {
//        callExtractorJar(filePath, destinationPath, dotFile, commandLineOutput);
        compileJavaFile(filePath, destinationPath, false, dotFile);
    }


    @Override
    public void extractProtoAllFilesInDirectory(String projectPath, String destinationPath, boolean dotFile, boolean commandLineOutput) {
        FileExtractor fileExtractor = new FileExtractor();
        List<String> allFilePaths = fileExtractor.getAllFilePaths(projectPath);
        allFilePaths.forEach(fp -> callExtractorJar(fp, destinationPath, dotFile, commandLineOutput));

    }


    private void compileJavaFile(String filePath, String destinationPath, boolean verboseDot, boolean dotFile) {
        try {
            Path fileAsPath = Paths.get(filePath);
            String name = fileAsPath.getFileName().toString();
            String content = new String(Files.readAllBytes(fileAsPath));
            TestCompilation compilation = TestCompilation.compileWithoutAnalyse(name, content);
            FeatureGraph featureGraph = FeaturePlugin.createFeatureGraph(compilation.compilationUnit(), compilation.context());
            FeaturePlugin.writeOutput(featureGraph, destinationPath, verboseDot, dotFile);
        } catch (IOException e) {
            LOGGER.error("Can not read java content", e);
        }

    }

    private void callExtractorJar(String filePath, String destinationPath, boolean dotFile, boolean commandLineOutput) {
        try {
            ProcessBuilder processBuilder;
            String[] command = {"javac", "-cp", jarPath, "-Xplugin:" + INNER_QUOTE + "FeaturePlugin",
                    destinationPath, dotFile + INNER_QUOTE, filePath};
            if (commandLineOutput) {
                processBuilder = new ProcessBuilder().inheritIO().command(command);
            } else {
                processBuilder = new ProcessBuilder().command(command);
            }
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Feature extraction failed")) {
                    LOGGER.error(line);
                }
            }
            int processCode = process.waitFor();
            LOGGER.info("Executor finished with code: " + processCode);
        } catch (IOException | InterruptedException exception) {
            LOGGER.error("Error calling Extractor!", exception);
        }
    }
}
