package de.sybit.mlz.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FileExtractor {

    public List<String> getAllFilePaths(String startPath) {
        List<String> allFilePaths = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get(startPath))) {
            paths.filter(path -> path.toString().endsWith(".java")).forEach(file -> allFilePaths.add(file.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allFilePaths;
    }
}
