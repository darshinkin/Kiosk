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
	
    public static final String deployPath = "/FriendKiosk";
    public static final String releaseFileName = "/FriendKiosk/release.txt";
    public static final String friendPlistFileName = "/FriendMobile.plist";
    public static final String friendIpaFileName = "/FriendMobile.ipa";
    public static final String apkFileName = "/friend.apk";
    public static final String sbgPlistFileName = "/sbg.plist";
    public static final String sbgIpaFileName = "/sbg.ipa";

    private static Properties properties;


    public Settings() throws AppException {
        InputStream inputStream = Settings.class.getResourceAsStream(CONFIG);
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

    public String getFileName(String os) {
        return properties.getProperty(os.toLowerCase());
    }
}
