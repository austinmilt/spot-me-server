package com.austinmilt.spotme;

public class Env {
    private Env() {
        // do not instantiate util class
    }

    public static final String HELLO_WHO = System.getenv("HELLO_WHO");
    public static final String OPEN_AI_API_KEY = System.getenv("OPEN_AI_API_KEY");
}
