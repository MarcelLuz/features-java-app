package de.sybit.mlz.extractors.impl;

import de.sybit.mlz.extractors.Extractor;
import de.sybit.mlz.utils.Compilation;
import de.sybit.mlz.utils.FileExtractor;
import org.apache.log4j.Logger;
import uk.ac.cam.acr31.features.javac.FeaturePlugin;
import uk.ac.cam.acr31.features.javac.graph.FeatureGraph;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FeatureJavacExtractor implements Extractor {

    private final static Logger LOGGER = Logger.getLogger(FeatureJavacExtractor.class.getName());

    @Override
    public void extractProtoFromSingleFile(String filePath, String destinationPath, boolean dotFile, boolean verboseDot) {
        compileJavaFile(filePath, destinationPath, verboseDot, dotFile);
    }


    @Override
    public void extractProtoAllFilesInDirectory(String projectPath, String destinationPath, boolean dotFile, boolean verboseDot) {
        FileExtractor fileExtractor = new FileExtractor();
        List<String> allFilePaths = fileExtractor.getAllFilePaths(projectPath);
        allFilePaths.forEach(filePath -> compileJavaFile(filePath, destinationPath, verboseDot, dotFile));

    }

    private void compileJavaFile(String filePath, String destinationPath, boolean verboseDot, boolean dotFile) {
        try {
            Path fileAsPath = Paths.get(filePath);
            String name = fileAsPath.getFileName().toString();
            String content = new String(Files.readAllBytes(fileAsPath));
            Compilation compilation = Compilation.compile(name, content);
            FeatureGraph featureGraph = FeaturePlugin.createFeatureGraph(compilation.compilationUnit(), compilation.context());
            FeaturePlugin.writeOutput(featureGraph, destinationPath, verboseDot, dotFile);
        } catch (IOException e) {
            LOGGER.error("can not parse java compilation task", e);
        }

    }
}
