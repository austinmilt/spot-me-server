package com.austinmilt.spotme;

import java.util.Map;

import com.google.gson.Gson;

public class ApiResponse<T> {
    private static final Gson GSON = new Gson();

    private final int code;
    private final T result;
    private final String error;

    private ApiResponse(final int code, final T result, final String error) {
        this.code = code;
        this.result = result;
        this.error = error;
    }

    public static <R> ApiResponse<R> ok(final R result) {
        return new ApiResponse<>(0, result, null);
    }

    public static ApiResponse<Void> notAllowlistedError() {
        return new ApiResponse<Void>(3, null, "Spotify member not allowlisted.");
    }

    public static ApiResponse<Void> clientError(final String error) {
        return new ApiResponse<Void>(1, null, error);
    }

    public static ApiResponse<Void> serverError(final String error) {
        return new ApiResponse<>(2, null, error);
    }

    public String toResponseJson() {
        if (code != 0) {
            return GSON.toJson(Map.of("code", code, "error", error));

        } else {
            return GSON.toJson(Map.of("code", code, "result", result));
        }
    }
}
