package com.sbrf.appkiosk.services;

import com.sbrf.appkiosk.Settings;
import com.sbrf.appkiosk.VersionQueryResponse;
import com.sbrf.appkiosk.exceptions.AppException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.io.*;
import java.util.List;

public class ApplicationService {
    private static final Logger LOGGER = LogManager.getLogger(ApplicationService.class);

    @Inject
    Settings settings;

    private static final String EXPLANATION_POINT = "!";


    public ApplicationService() {
    }

    public VersionQueryResponse getVersionQueryResponse(String os) throws AppException {
        String fileName = settings.getFileName(os);
        if (fileName == null) {
            throw new AppException(500, 500, String.format("For %s was not found path of the release file in the settings %s.", os, Settings.CONFIG),
                    "fileName is null", null);
        }

        VersionQueryResponse response = new VersionQueryResponse();
        String currentVersion = null;
        List<String> releaseNotes = response.getReleaseNotes();
        File releaseNotesFile = new File(fileName);
        if (releaseNotesFile.exists()) {
            currentVersion = getCurrentVersion(releaseNotesFile, releaseNotes);
        } else {
            throw new AppException(500, 500, String.format("File with name %s is not exist.", fileName),
                    "File is not exist", null);
        }
        if (currentVersion == null || currentVersion.isEmpty()) {
            throw new AppException(500, 500, String.format("Current version is null of empty. Filename: %s, OS: %s ", fileName, os),
                    "Current version is null of empty.", null);
        }
        response.setMandatory(currentVersion.contains(EXPLANATION_POINT));
        response.setVersion(currentVersion.replace(EXPLANATION_POINT, ""));

        return response;
    }

    String getCurrentVersion(final File releaseNotesFile, List<String> releaseNotes) throws AppException {
        String currentVersion = "";
        try (BufferedReader br = new BufferedReader(new FileReader(releaseNotesFile))) {
            currentVersion = br.readLine();
            while (true){
                String f = br.readLine();
                if (f!=null){
                    releaseNotes.add(f);
                } else {
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            LOGGER.error(e.getMessage());
            throw new AppException(500, 500, String.format("File with name %s was not found.", releaseNotesFile),
                    "File Not Found", null);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            throw new AppException();
        }
        return currentVersion;
    }
}
