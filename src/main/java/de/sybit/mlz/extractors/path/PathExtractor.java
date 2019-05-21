package de.sybit.mlz.extractors.path;

import JavaExtractor.Common.CommandLineValues;
import JavaExtractor.ExtractFeaturesTask;
import org.apache.log4j.Logger;
import org.kohsuke.args4j.CmdLineException;

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

public class PathExtractor {

    private final static Logger LOGGER = Logger.getLogger(PathExtractor.class.getName());

    private final static String MAX_PATH_LENGTH_KEY = "--max_path_length";
    private final static String MAX_PATH_WIDTH_KEY = "--max_path_width";
    private final static String MIN_CODE_LEN = "--min_code_len";
    private final static String MAX_CODE_LEN = "--max_code_len";
    private final static String MAX_CHILD_ID = "--max_child_id";

    private String maxPathLength;
    private String maxPathWidth;
    private String minCodeLen;
    private String maxCodeLen;
    private String maxChildId;

    private CommandLineValues extractorConfig;

    public PathExtractor(int maxPathLength, int maxPathWidth, int minCodeLen, int maxCodeLen, int maxChildId) {
        this.maxPathLength = String.valueOf(maxPathLength);
        this.maxPathWidth = String.valueOf(maxPathWidth);
        this.minCodeLen = String.valueOf(minCodeLen);
        this.maxCodeLen = String.valueOf(maxCodeLen);
        this.maxChildId = String.valueOf(maxChildId);

        try {
            this.extractorConfig = buildExtractorConfig();
        } catch (CmdLineException e) {
            LOGGER.error("cant pars path-extractor config", e);
        }
    }

    public void extractPathsFromSingleFile(String filePath, String destinationPath) {
        Path fileAsPath = Path.of(filePath);
        Path destinationPathAsPath = Path.of(destinationPath);
        InternalExtractPathsTask extractFeaturesTask = new InternalExtractPathsTask(extractorConfig, fileAsPath);
        extractFeaturesTask.processFileSingleFile(fileAsPath.getFileName().toString(), destinationPathAsPath);
    }

    public void extractPathsFromAllFilesInDirectory(String projectPath, String destinationPath, int numThreads) {
        Path destinationPathAsPath = Path.of(destinationPath);
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(numThreads);
        LinkedList<ExtractFeaturesTask> tasks = new LinkedList<>();
        try {
            Files.walk(Paths.get(projectPath)).filter(Files::isRegularFile)
                    .filter(p -> p.toString().toLowerCase().endsWith(".java")).forEach(fileAsPath -> {
                InternalExtractPathsTask task = new InternalExtractPathsTask(extractorConfig, fileAsPath);
                task.setDestinationPath(destinationPathAsPath);
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
            LOGGER.error("multithreading for paths failed",e);
        } finally {
            executor.shutdown();
        }
        Objects.requireNonNull(tasksResults).forEach(f -> {
            try {
                f.get();
            } catch (InterruptedException | ExecutionException e) {
                LOGGER.error("multithreading for paths failed",e);
            }
        });
    }

    private CommandLineValues buildExtractorConfig() throws CmdLineException {
        return new CommandLineValues(
                MAX_PATH_LENGTH_KEY, maxPathLength,
                MAX_PATH_WIDTH_KEY, maxPathWidth,
                MIN_CODE_LEN, minCodeLen,
                MAX_CODE_LEN, maxCodeLen,
                MAX_CHILD_ID, maxChildId
        );
    }
}
