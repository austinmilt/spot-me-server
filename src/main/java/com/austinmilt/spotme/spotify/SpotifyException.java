package com.austinmilt.spotme.spotify;

public class SpotifyException extends RuntimeException {
    private final Code code;

    private SpotifyException(final String message, final Code code) {
        super(message);
        this.code = code;
    }

    public static SpotifyException notAllowlisted() {
        return new SpotifyException("User not allowlisted.", Code.NOT_ALLOWLISTED);
    }

    public static SpotifyException general(final String message) {
        return new SpotifyException(message, Code.GENERAL);
    }

    public Code geCode() {
        return code;
    }

    public static enum Code {
        NOT_ALLOWLISTED,
        GENERAL
    }
}
