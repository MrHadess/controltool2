package com.mh.controltool2.context;

import com.mh.controltool2.serialize.BaseDataTypeChange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class ConfigReaderToFramework implements ConfigReader {

    private static Logger logger = LoggerFactory.getLogger(ConfigReaderToFramework.class);

    private Properties properties = new Properties();

    /*
    * If file empty
    * */
    public ConfigReaderToFramework(String propertiesFileName) {
        if (propertiesFileName == null) {
            logger.warn("properties file value is null");
            return;
        }
        File tempFile = new File(propertiesFileName);
        if (!tempFile.isAbsolute()) {
            // load class path to file
            URL url = ConfigReaderToFramework.class.getClassLoader().getResource(propertiesFileName);
            if (url == null) {
                logger.warn(String.format("Unable file to read config file:'classpath:%s'",propertiesFileName));
                return;
            }
            tempFile = new File(url.getFile());
        }

        // check file exists
        if (!tempFile.exists()) {
            logger.warn(String.format("Unable file to read config file:'%s'",propertiesFileName));
            return;
        }

        try {
            loadPropertiesFile(tempFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadPropertiesFile(File file) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            properties.load(fileInputStream);
        }
    }

    @Override
    public String readValue(String key) {
        return properties.getProperty(key);
    }

    @Override
    public <T> T readValue(String key, Class<T> tagValueType) {
        Object data = BaseDataTypeChange.stringToBaseData(tagValueType.getName(),properties.getProperty(key));
        return tagValueType.cast(data);
    }

    @Override
    public String toString() {
        return "ConfigReaderToFramework{" +
                "properties=" + properties +
                '}';
    }
}
