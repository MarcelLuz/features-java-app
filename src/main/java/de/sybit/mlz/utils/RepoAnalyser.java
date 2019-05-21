package de.sybit.mlz.utils;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class RepoAnalyser {

    private final static Logger LOGGER = Logger.getLogger(RepoAnalyser.class.getName());

    public static int getNumberOfFilesInRepo(String startPath) {
        int counter = 0;
        try (Stream<Path> paths = Files.walk(Paths.get(startPath))) {
            counter = (int) paths.filter(path -> path.toString().endsWith(".java")).count();
        } catch (IOException ioException) {
            LOGGER.error("Can not count all Files in Directory or Subdirectory", ioException);
        }
        return counter;
    }
}
