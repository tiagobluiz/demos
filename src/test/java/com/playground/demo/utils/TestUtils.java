package com.playground.demo.utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;

public class TestUtils {

    public static <T> String readFileAsString(Class<T> testClass, String fileName) throws IOException {

        try {
            URL resource = testClass.getResource(fileName);

            assert resource != null : "File was not found";

            File file = new File(resource.toURI());

            return FileUtils.readFileToString(file, Charset.defaultCharset());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("The file name is invalid");
        }
    }
}
