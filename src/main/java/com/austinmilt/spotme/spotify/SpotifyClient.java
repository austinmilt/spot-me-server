package com.austinmilt.spotme.spotify;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.google.gson.Gson;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SpotifyClient {
    private final OkHttpClient httpClient;

    private SpotifyClient(final OkHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public static SpotifyClient make() {
        return new SpotifyClient(makeHttpClient());
    }

    private static OkHttpClient makeHttpClient() {
        return new OkHttpClient()
                .newBuilder()
                .readTimeout(1000, TimeUnit.MILLISECONDS)
                .writeTimeout(1000, TimeUnit.MILLISECONDS)
                .build();
    }

    public Set<String> getSavedTrackGenres(final String accessToken) throws IOException {
        final Set<String> savedTrackArtistIds = getSavedTrackArtistIds(accessToken);
        return getArtistGenres(savedTrackArtistIds, accessToken);
    }

    private Set<String> getSavedTrackArtistIds(final String accessToken) throws IOException {
        final HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("api.spotify.com")
                .addPathSegment("v1")
                .addPathSegment("me")
                .addPathSegment("tracks")
                .addQueryParameter("time_range", "medium_term")
                .addQueryParameter("limit", "50")
                .addQueryParameter("offset", "0")
                .build();

        final Request request = new Request.Builder()
                .get()
                .addHeader("Authorization", String.format("Bearer %s", accessToken))
                .url(url)
                .build();

        // TODO async
        // TODO error handling
        final Response response = httpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            final String resultString = response.body().string();
            final SavedTracksResponse savedTracks = new Gson().fromJson(resultString, SavedTracksResponse.class);
            return savedTracks.getItems()
                    .stream()
                    .map(SavedTracksResponse.SavedTracksItem::getTrack)
                    .flatMap(track -> track.getArtists().stream())
                    .map(SavedTracksResponse.Artist::getId)
                    .collect(Collectors.toSet());

        } else {
            // TODO better
            throw new RuntimeException("Invalid response with code " + response.code());
        }
    }

    // https://developer.spotify.com/documentation/web-api/reference/get-multiple-artists
    private Set<String> getArtistGenres(final Set<String> artistIds, final String accessToken) throws IOException {
        final HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("api.spotify.com")
                .addPathSegment("v1")
                .addPathSegment("artists")
                .addQueryParameter("ids", String.join(",", artistIds))
                .build();

        final Request request = new Request.Builder()
                .get()
                .addHeader("Authorization", String.format("Bearer %s", accessToken))
                .url(url)
                .build();

        // TODO async
        // TODO error handling
        final Response response = httpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            final String resultString = response.body().string();
            final GetArtistsResponse artistsResponse = new Gson().fromJson(resultString, GetArtistsResponse.class);
            return artistsResponse.getArtists()
                    .stream()
                    .map(GetArtistsResponse.Artist::getGenres)
                    .flatMap(genres -> genres.stream())
                    .collect(Collectors.toSet());

        } else {
            // TODO better
            throw new RuntimeException("Invalid response with code " + response.code());
        }
    }
}
