package de.sybit.mlz.utils;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {
    private static final Logger LOGGER = Logger.getLogger(PropertiesLoader.class.getName());

    public static Properties loadProperties(String resourceFileName) {
        Properties configuration = new Properties();
        InputStream inputStream = PropertiesLoader.class
                .getClassLoader()
                .getResourceAsStream(resourceFileName);
        try {
            assert inputStream != null;
            configuration.load(inputStream);
            inputStream.close();
        } catch (IOException ioException) {
            LOGGER.error("Cant read Properties from Resources", ioException);
        }

        return configuration;
    }
}
