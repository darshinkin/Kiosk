package com.sbrf.appkiosk;

import com.google.common.io.ByteStreams;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TestHelper {
    public static String getResourceContent(String resource) throws IOException {
        return new String(ByteStreams.toByteArray(TestHelper.class.getResourceAsStream(resource)), StandardCharsets.UTF_8);
    }
}
