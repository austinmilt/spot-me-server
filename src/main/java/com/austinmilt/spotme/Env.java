package com.austinmilt.spotme;

public class Env {
    private Env() {
        // do not instantiate util class
    }

    public static final String OPEN_AI_API_KEY = parseString("OPEN_AI_API_KEY");
    public static final int GENRE_SAMPLE_SIZE = parseInt("GENRE_SAMPLE_SIZE");

    private static int parseInt(final String key) {
        final String candidate = parseString(key);
        return Integer.parseInt(candidate);
    }

    private static String parseString(final String key) {
        final String candidate = System.getenv(key);
        if (candidate == null) {
            // TODO specialized exception
            throw new RuntimeException("Missing required environment variable " + key);
        }
        return candidate;
    }
}
