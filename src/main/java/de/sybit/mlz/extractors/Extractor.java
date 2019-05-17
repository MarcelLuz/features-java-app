package de.sybit.mlz.extractors;

public interface Extractor {
    public void extractProtoFromSingleFile(String filePath, String destinationPath, boolean dotFile, boolean commandLineOutput);
    public void extractProtoAllFilesInDirectory(String projectPath, String destinationPath, boolean dotFile, boolean commandLineOutput);
}
