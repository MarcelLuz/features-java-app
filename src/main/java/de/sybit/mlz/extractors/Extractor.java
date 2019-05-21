package de.sybit.mlz.extractors;

public interface Extractor {
    public void extractProtoFromSingleFile(String filePath, String destinationPath, boolean dotFile, boolean verboseDot);
    public void extractProtoAllFilesInDirectory(String projectPath, String destinationPath, boolean dotFile, boolean verboseDot);
}
