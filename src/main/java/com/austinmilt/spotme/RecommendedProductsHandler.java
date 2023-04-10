package com.austinmilt.spotme;

import java.util.Optional;
import java.util.Set;

import com.austinmilt.spotme.gpt.OpenAiClient;
import com.austinmilt.spotme.spotify.SpotifyClient;
import com.austinmilt.spotme.spotify.SpotifyException;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;

public class RecommendedProductsHandler implements HttpFunction {
    private final SpotifyClient spotifyClient = SpotifyClient.make();
    private final OpenAiClient openAiClient = OpenAiClient.make();

    @Override
    public void service(HttpRequest request, HttpResponse response) throws Exception {
        if (isPrefetch(request)) {
            handlePrefetch(response);

        } else {
            handleRequest(request, response);
        }
    }

    private boolean isPrefetch(HttpRequest request) {
        return request.getMethod().equalsIgnoreCase("OPTIONS");
    }

    private void handlePrefetch(HttpResponse response) {
        response.appendHeader("Access-Control-Allow-Origin", "*");
        response.appendHeader("Access-Control-Allow-Methods", "GET");
        response.appendHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setStatusCode(200);
    }

    private void handleRequest(HttpRequest request, HttpResponse response) throws Exception {
        response.appendHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("application/json");

        final Optional<String> accessTokenOption = request.getFirstQueryParameter("access_token");
        if (accessTokenOption.isEmpty()) {
            response.setStatusCode(400);
            response.getWriter().write(ApiResponse.clientError("Missing required access token.").toResponseJson());
            return;
        }

        try {
            final String accessToken = accessTokenOption.get();
            final Set<String> genres = spotifyClient.getSavedTrackGenres(accessToken);
            final Set<String> recommendations = openAiClient.getProductRecommendations(genres);
            response.setStatusCode(200);
            response.getWriter().write(ApiResponse.ok(recommendations).toResponseJson());

        } catch (SpotifyException e) {
            if (e.geCode() == SpotifyException.Code.NOT_ALLOWLISTED) {
                response.setStatusCode(400);
                response.getWriter().write(ApiResponse.notAllowlistedError().toResponseJson());

            } else {
                response.setStatusCode(500);
                response.getWriter().write(ApiResponse.serverError(e.getMessage()).toResponseJson());
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.getWriter().write(ApiResponse.serverError(e.getMessage()).toResponseJson());
        }
    }
}
