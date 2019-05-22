package de.sybit.mlz.extractors.path;

import JavaExtractor.Common.CommandLineValues;
import JavaExtractor.ExtractFeaturesTask;
import JavaExtractor.FeatureExtractor;
import JavaExtractor.FeaturesEntities.ProgramFeatures;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class InternalExtractPathsTask extends ExtractFeaturesTask {

    private final static Logger LOGGER = Logger.getLogger(InternalExtractPathsTask.class.getName());
    private Path destinationPath;
    private String datasetName;

    @Override
    public Void call() {
        processMultipleFiles();
        return null;
    }

    public InternalExtractPathsTask(CommandLineValues commandLineValues, Path path) {
        super(commandLineValues, path);
    }

    public void processFileSingleFile(String fileName, Path destinationPath) {
        ArrayList<ProgramFeatures> features;
        try {
            features = this.extractSingleFile();
        } catch (IOException e) {
            LOGGER.error("error extract single file", e);
            return;
        }

        if (features != null) {
            try {
                String toPrint = this.featuresToString(features);
                String c2cFileName = fileName.replace(".java", ".c2s");
                File baseDirectory = destinationPath.toFile();
                File fileInDirectory = new File(baseDirectory, c2cFileName);
                FileUtils.writeStringToFile(fileInDirectory, toPrint);
            } catch (IOException e) {
                LOGGER.error("error write extracted paths from single file", e);
            }
        }
    }

    public synchronized void processMultipleFiles() {
        FileWriter fileWriter;
        BufferedWriter bufferedWriter;
        File baseDirectory = getDestinationPath().toFile();
        String datasetName = getDatasetName();
        File fileInDirectory = new File(baseDirectory, datasetName + ".c2s");

        ArrayList<ProgramFeatures> features;
        try {
            features = this.extractSingleFile();

            if (features != null) {
                String toPrint = this.featuresToString(features);
                fileWriter = new FileWriter(fileInDirectory.getAbsoluteFile(), true);
                bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(toPrint);
                bufferedWriter.close();
            }
        } catch (IOException e) {
            LOGGER.error("error extract multiple files", e);
        }
    }


    private ArrayList<ProgramFeatures> extractSingleFile() throws IOException {
        if (getM_CommandLineValues().MaxFileLength > 0 && Files.lines(getFilePath(),
                Charset.defaultCharset()).count() > (long) getM_CommandLineValues().MaxFileLength) {
            return new ArrayList<>();
        } else {
            String code;
            try {
                code = new String(Files.readAllBytes(getFilePath()));
            } catch (IOException var3) {
                var3.printStackTrace();
                code = "";
            }

            FeatureExtractor featureExtractor = new FeatureExtractor(getM_CommandLineValues());
            return featureExtractor.extractFeatures(code);
        }
    }

    public Path getDestinationPath() {
        return destinationPath;
    }

    public void setDestinationPath(Path destinationPath) {
        this.destinationPath = destinationPath;
    }

    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }
}
