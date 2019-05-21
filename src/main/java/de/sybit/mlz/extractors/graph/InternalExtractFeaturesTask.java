package de.sybit.mlz.extractors.graph;

import de.sybit.mlz.utils.Compilation;
import org.apache.log4j.Logger;
import uk.ac.cam.acr31.features.javac.FeaturePlugin;
import uk.ac.cam.acr31.features.javac.graph.FeatureGraph;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

public class InternalExtractFeaturesTask implements Callable<Void> {

    private final static Logger LOGGER = Logger.getLogger(InternalExtractFeaturesTask.class.getName());

    private boolean verboseDot;
    private boolean dotFile;
    private Path fileName;
    private Path destinationPath;

    public InternalExtractFeaturesTask(Path fileName, Path destinationPath, boolean dotFile, boolean verboseDot) {
        this.fileName = fileName;
        this.destinationPath = destinationPath;
        this.dotFile = dotFile;
        this.verboseDot = verboseDot;
    }

    @Override
    public Void call() {
        processMultipleFiles();
        return null;
    }


    public void processFileSingleFile() {
        compileJavaFile(fileName,destinationPath,verboseDot,dotFile);
    }

    public void processMultipleFiles() {
        compileJavaFile(fileName,destinationPath,verboseDot,dotFile);
    }

    private void compileJavaFile(Path filePath, Path destinationPath, boolean verboseDot, boolean dotFile) {
        try {
            String name = filePath.getFileName().toString();
            String content = new String(Files.readAllBytes(filePath));
            Compilation compilation = Compilation.compile(name, content);
            FeatureGraph featureGraph = FeaturePlugin.createFeatureGraph(compilation.compilationUnit(), compilation.context());
            FeaturePlugin.writeOutput(featureGraph, destinationPath.toString(), verboseDot, dotFile);
        } catch (IOException e) {
            LOGGER.error("can not parse java compilation task", e);
        }
    }
}
