package de.sybit.mlz.extractors.impl;

import de.sybit.mlz.extractors.Extractor;
import org.apache.log4j.Logger;

//TODO: Impl.
public class PathExtractor implements Extractor {

    private final static Logger LOGGER = Logger.getLogger(PathExtractor.class.getName());
    private String jarPath;

    public PathExtractor(String jarPath) {
        this.jarPath = jarPath;
    }

    @Override
    public void extractProtoFromSingleFile(String filePath, String destinationPath, boolean dotFile, boolean commandLineOutput) {

    }

    @Override
    public void extractProtoAllFilesInDirectory(String projectPath, String destinationPath, boolean dotFile, boolean commandLineOutput) {

    }

    private void callExtractorJar(String filePath, String destinationPath, boolean dotFile, boolean commandLineOutput) {

    }
}
