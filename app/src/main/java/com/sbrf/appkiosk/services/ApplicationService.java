package com.sbrf.appkiosk.services;

import com.google.gson.Gson;
import com.sbrf.appkiosk.Settings;
import com.sbrf.appkiosk.VersionQueryResponse;
import com.sbrf.appkiosk.exceptions.AppException;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.ws.rs.core.UriInfo;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ApplicationService {
    private static final Logger LOGGER = LogManager.getLogger(ApplicationService.class);
    public static final String IPA = "ipa";
    public static final String IPA_FILE_STUB = "{ipa_file}";

    @Inject
    Settings settings;

    private static final String EXPLANATION_POINT = "!";


    public ApplicationService() {
    }

    public VersionQueryResponse getVersionQueryResponse(String os, String app) throws AppException {
        String fileName = settings.getFileReleaseName(os, app);
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
        try (BufferedReader br = new BufferedReader(new InputStreamReader( new FileInputStream(releaseNotesFile), "UTF8"))) {
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

    public File getFile(String os, String app, String fileType) throws AppException {
        String fileName = Settings.getFileName(os, app, fileType);
        File file = getFile(fileName);
        return file;
    }

    public InputStream getIosFile(String app, UriInfo uriDetails) throws AppException, IOException {
        String fileName = Settings.getIosPlist(app);
        File file = getFile(fileName);
        String plist = FileUtils.readFileToString(file, "UTF-8");
        System.out.println(String.format("--------- URI's requeste. AbsolutePath: %s, BaseUri: %s, RequestUri: %s",
                uriDetails.getAbsolutePath().toString(), uriDetails.getBaseUri().toString(), uriDetails.getRequestUri().toString()));
        String baseUri = uriDetails.getBaseUri().toString();
        String baseUriWithoutPort = baseUri.replace(":"+String.valueOf(uriDetails.getBaseUri().getPort()), "");
        StringBuilder urlIpa = new StringBuilder(baseUriWithoutPort);
        urlIpa.append(Settings.FILE)
                .append(Settings.SLASH).append(Settings.IOS)
                .append(Settings.SLASH).append(app)
                .append(Settings.SLASH).append(IPA);
        LOGGER.info("Created ipa file: "+urlIpa.toString());
        String modifedPlist=plist.replace(IPA_FILE_STUB, urlIpa.toString());
        return new ByteArrayInputStream(modifedPlist.getBytes(StandardCharsets.UTF_8));
    }

    public File getAndroidFile(String app) throws AppException {
        String fileName = Settings.getAndroidApk(app);
        File file = getFile(fileName);
        return file;
    }

    private File getFile(String fileName) throws AppException {
        File file = new File(fileName);
        if (!file.exists()) {
            throw new AppException(500, 500, String.format("File with name %s is not exist.", fileName),
                    String.format("File name %s is not exist. Method: ApplicationService.getAndroidFile", fileName), null);
        }
        return file;
    }

    //todo depricated method
    public String getVersionOld(String version) throws AppException {
        System.out.println("AppService.getVersion()" + version);

        String fileName = Settings.getReleaseForOldVersion();
        if (fileName == null) {
            throw new AppException(500, 500, String.format("For ios was not found path of the release file in the settings %s.", Settings.CONFIG_OLD),
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
            throw new AppException(500, 500, String.format("Current version is null of empty. Filename: %s, OS: ios ", fileName),
                    "Current version is null of empty.", null);
        }
        if (version.equalsIgnoreCase(currentVersion)) {
            response.setStatus("ok");
        } else {
            response.setMandatory(currentVersion.contains(EXPLANATION_POINT));
            response.setVersion(currentVersion.replace(EXPLANATION_POINT, ""));
            response.setStatus("update");
            response.setReleaseNotes(releaseNotes);
        }

        Gson gson = new Gson();
        return gson.toJson(response);
    }

    public InputStream getFileFriendMobilePlist(UriInfo uriDetails) throws AppException, IOException {
        String fileName = Settings.getFriendPlistFileName();
        File file = getFile(fileName);
        String plist = FileUtils.readFileToString(file, "UTF-8");
        System.out.println(String.format("--------- URI's requeste. AbsolutePath: %s, BaseUri: %s, RequestUri: %s",
                uriDetails.getAbsolutePath().toString(), uriDetails.getBaseUri().toString(), uriDetails.getRequestUri().toString()));
        String modifedPlist=plist.replace(IPA_FILE_STUB, Settings.getFriendIpaFileName());
        return new ByteArrayInputStream(modifedPlist.getBytes(StandardCharsets.UTF_8));
    }

    public InputStream getFileSbgMobilePlist(UriInfo uriDetails) throws AppException, IOException {
        String fileName = Settings.getSbgPlistFileName();
        File file = getFile(fileName);
        String plist = FileUtils.readFileToString(file, "UTF-8");
        System.out.println(String.format("--------- URI's requeste. AbsolutePath: %s, BaseUri: %s, RequestUri: %s",
                uriDetails.getAbsolutePath().toString(), uriDetails.getBaseUri().toString(), uriDetails.getRequestUri().toString()));
        String modifedPlist=plist.replace(IPA_FILE_STUB, Settings.getSbgIpaFileName());
        return new ByteArrayInputStream(modifedPlist.getBytes(StandardCharsets.UTF_8));
    }
}
