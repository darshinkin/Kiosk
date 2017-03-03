package com.sbrf.appkiosk;

import com.sbrf.appkiosk.exceptions.AppException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Settings {
    private static final Logger LOGGER = LogManager.getLogger(Settings.class);

    public static final String CONFIG = "config.properties";

    public static final String SLASH = "/";
    public static final String FILE = "file";
    public static final String ANDROID = "android";
    public static final String IOS = "ios";
    private static final Object RELEASE = "release";
    public static final String PLIST = "plist";

    private static Properties properties = new Properties();

    public Settings() throws AppException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(CONFIG);
        if (inputStream == null) {
            LOGGER.error(String.format("File %s has not been loaded.", CONFIG));
            throw new AppException(500, 500, String.format("File %s has not been loaded.",
                    CONFIG),String.format("File %s has not been loaded.", CONFIG), null);
        }
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getAndroidApk(String app) {
        return properties.getProperty(String.format("%s.%s", ANDROID, app));
    }

    public static String getIosPlist(String app) {
        return properties.getProperty(String.format("%s.%s.%s", IOS, app, PLIST));
    }

    public static String getFileName(String os, String app, String fileType) {
        return properties.getProperty(String.format("%s.%s.%s", os, app, fileType));
    }

    public String getFileReleaseName(String os, String app) {
        return properties.getProperty(String.format("%s.%s.%s", os, app, RELEASE));
    }
}
