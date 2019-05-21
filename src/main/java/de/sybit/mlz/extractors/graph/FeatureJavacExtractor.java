package de.sybit.mlz.extractors.graph;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class FeatureJavacExtractor {

    private final static Logger LOGGER = Logger.getLogger(FeatureJavacExtractor.class.getName());

    public void extractProtoFromSingleFile(String filePath, String destinationPath, boolean dotFile, boolean verboseDot) {
        Path fileAsPath = Path.of(filePath);
        Path destinationPathAsPath = Path.of(destinationPath);
        InternalExtractFeaturesTask internalExtractfeaturesTask = new InternalExtractFeaturesTask(
                fileAsPath,
                destinationPathAsPath,
                dotFile,
                verboseDot
        );
        internalExtractfeaturesTask.processFileSingleFile();
    }


    public void extractProtoFromAllFilesInDirectory(String projectPath, String destinationPath, boolean dotFile, boolean verboseDot, int numThreads) {
        Path destinationPathAsPath = Path.of(destinationPath);
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(numThreads);
        LinkedList<InternalExtractFeaturesTask> tasks = new LinkedList<>();
        try {
            Files.walk(Paths.get(projectPath)).filter(Files::isRegularFile)
                    .filter(p -> p.toString().toLowerCase().endsWith(".java")).forEach(fileAsPath -> {
                InternalExtractFeaturesTask task = new InternalExtractFeaturesTask(
                        fileAsPath,
                        destinationPathAsPath,
                        dotFile,
                        verboseDot
                );
                tasks.add(task);
            });
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        List<Future<Void>> tasksResults = null;
        try {
            tasksResults = executor.invokeAll(tasks);
        } catch (InterruptedException e) {
            LOGGER.error("multithreading for proto failed",e);
        } finally {
            executor.shutdown();
        }
        Objects.requireNonNull(tasksResults).forEach(f -> {
            try {
                f.get();
            } catch (InterruptedException | ExecutionException e) {
                LOGGER.error("multithreading for proto failed",e);
            }
        });
    }
}
