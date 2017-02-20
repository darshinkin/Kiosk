package com.sbrf.appkiosk.services;

import com.sbrf.appkiosk.Settings;
import com.sbrf.appkiosk.VersionQueryResponse;
import com.sbrf.appkiosk.exceptions.AppException;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(MockitoJUnitRunner.class)
//@PrepareForTest(Settings.class)
public class ApplicationServiceTest {
    private static final String RELEASE_NOTES_FILE = "releaseNote.txt";

    @Mock
    ApplicationService applicationServiceMock;
//    @InjectMocks
//    private Settings settingsMock;

    @Test
    public void testGetCurrentVersion() throws AppException {
        ApplicationService applicationService = new ApplicationService();
        List<String> releaseNotes = new ArrayList<>();
        File file = new File(ApplicationService.class.getResource("/releaseNote.txt").getFile());
        String currentVersion = applicationService.getCurrentVersion(file, releaseNotes);
        assertEquals("78512!", currentVersion);
        List<String> actualReleasNotes = Arrays.asList("The id field is a unique identifier for the greeting,",
                "and content is the textual representation of the greeting.",
                "To model the greeting representation, you create a resource representation class.",
                "Provide a plain old java object with fields, constructors, and accessors for the id and content data:");
        assertEquals(actualReleasNotes, releaseNotes);
    }

    @Test
    @Ignore
    public void testGetVersionQueryResponse() throws Exception {
        String os = "android";
        String fileName = ApplicationService.class.getResource("/releaseNote.txt").getFile();
        whenNew(Settings.class).withNoArguments().thenReturn(mock(Settings.class));
        VersionQueryResponse response = applicationServiceMock.getVersionQueryResponse(os);
        assertEquals("78512", response.getVersion());
        assertTrue(response.isMandatory());
        assertNull(response.getStatus());
    }
}
