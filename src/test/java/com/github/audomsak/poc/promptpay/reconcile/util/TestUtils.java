package com.github.audomsak.poc.promptpay.reconcile.util;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class TestUtils {
    private TestUtils() {}

    public static String getTestData(String path, Charset charset) throws IOException {
        return IOUtils.toString(requireNonNull(TestUtils.class.getClassLoader().getResourceAsStream(path)), charset);
    }

    public static String getMockedEndpoint(String endpoint) {
        return "mock:".concat(endpoint);
    }
}
