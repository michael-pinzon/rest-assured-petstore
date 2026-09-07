package com.petdog.api.config;

/**
 * Provides the Petstore connection settings used by the test client.
 *
 * <p>The system properties make it possible to point the suite to another
 * compatible environment without changing source code.</p>
 */
public final class ApiConfig {

    public static final String DEFAULT_BASE_URI = "https://petstore.swagger.io";
    public static final String DEFAULT_BASE_PATH = "/v2";

    private ApiConfig() {
        // Utility class.
    }

    /**
     * Returns the configured API host or the public Petstore host by default.
     *
     * @return API host without the version path
     */
    public static String baseUri() {
        return System.getProperty("petstore.baseUri", DEFAULT_BASE_URI);
    }

    /**
     * Returns the configured API version path.
     *
     * @return API base path
     */
    public static String basePath() {
        return System.getProperty("petstore.basePath", DEFAULT_BASE_PATH);
    }
}
