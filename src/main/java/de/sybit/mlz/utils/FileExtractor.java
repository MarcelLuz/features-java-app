package de.sybit.mlz.utils;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FileExtractor {

    private final static Logger LOGGER = Logger.getLogger(FileExtractor.class.getName());

    public List<String> getAllFilePaths(String startPath) {
        List<String> allFilePaths = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get(startPath))) {
            paths.filter(path -> path.toString().endsWith(".java")).forEach(file -> allFilePaths.add(file.toString()));
        } catch (IOException ioException) {
            LOGGER.error("Can not read all Files in Directory or Subdirectory",ioException);
        }
        return allFilePaths;
    }
}
