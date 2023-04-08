package com.austinmilt.spotme;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.austinmilt.spotme.gpt.OpenAiClient;
import com.austinmilt.spotme.spotify.SpotifyClient;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.google.gson.Gson;

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
        final Optional<String> accessTokenOption = request.getFirstQueryParameter("access_token");
        if (accessTokenOption.isEmpty()) {
            response.setStatusCode(400, "Missing URL param: access_token");
            return;
        }

        final String accessToken = accessTokenOption.get();
        final Set<String> genres = spotifyClient.getSavedTrackGenres(accessToken);
        final Set<String> recommendations = openAiClient.getProductRecommendations(genres);
        response.setStatusCode(200);
        response.appendHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("application/json");
        response.getWriter().write(new Gson().toJson(Map.of("result", recommendations)));
    }
}
