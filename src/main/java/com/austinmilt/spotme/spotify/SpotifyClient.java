package com.austinmilt.spotme.spotify;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.austinmilt.spotme.Env;
import com.google.gson.Gson;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SpotifyClient {
    private static final Gson GSON = new Gson();

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

    public Map<String, GenreMetadata> getSavedTrackGenres(final String accessToken) throws IOException {
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
            final SavedTracksResponse savedTracks = GSON.fromJson(resultString, SavedTracksResponse.class);
            return savedTracks.getItems()
                    .stream()
                    .map(SavedTracksResponse.SavedTracksItem::getTrack)
                    .flatMap(track -> track.getArtists().stream())
                    .map(SavedTracksResponse.Artist::getId)
                    .collect(Collectors.toSet());

        } else if (response.code() == 403) {
            throw SpotifyException.notAllowlisted();

        } else {
            throw SpotifyException.general("Invalid response with HTTP code " + response.code());
        }
    }

    // https://developer.spotify.com/documentation/web-api/reference/get-multiple-artists
    private Map<String, GenreMetadata> getArtistGenres(
            final Set<String> artistIds,
            final String accessToken) throws IOException {

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
            final GetArtistsResponse artistsResponse = GSON.fromJson(resultString, GetArtistsResponse.class);
            final Map<String, GenreMetadata> result = new HashMap<>();
            for (GetArtistsResponse.Artist artist : artistsResponse.getArtists()) {
                final GenreMetadata.Artist artistMeta = GenreMetadata.Artist.of(artist.getName(),
                        artist.getExternalUrls().get("spotify"),
                        selectArtistImage(artist.getImages()));

                for (String genre : artist.getGenres()) {
                    result.computeIfAbsent(genre, g -> new GenreMetadata()).addArtist(artistMeta);
                }
            }
            return result;

        } else if (response.code() == 403) {
            throw SpotifyException.notAllowlisted();

        } else {
            throw SpotifyException.general("Invalid response with HTTP code " + response.code());
        }
    }

    private String selectArtistImage(List<Image> images) {
        if (images.size() == 0) {
            return null;
        }
        Image closestImage = images.get(0);
        int closestSizeDiff = Math.abs(closestImage.getWidth() - Env.ARTIST_IMAGE_TARGET_WIDTH_PIXELS);
        for (int i = 1; i < images.size(); i++) {
            final int sizeDiff = Math.abs(images.get(i).getWidth() - Env.ARTIST_IMAGE_TARGET_WIDTH_PIXELS);
            if (sizeDiff < closestSizeDiff) {
                closestImage = images.get(i);
                closestSizeDiff = sizeDiff;
            }
        }
        return closestImage.getUrl();
    }
}
