package com.austinmilt.spotme.spotify;

import java.util.HashSet;
import java.util.Set;

public class GenreMetadata {
    private final Set<Artist> artists = new HashSet<>();

    public void addArtist(final Artist artist) {
        this.artists.add(artist);
    }

    public static class Artist {
        private final String name;
        private final String spotifyPageUrl;
        private final String imageUrl;

        private Artist(final String name, final String spotifyPageUrl, final String imageUrl) {
            this.name = name;
            this.spotifyPageUrl = spotifyPageUrl;
            this.imageUrl = imageUrl;
        }

        public static Artist of(final String name, final String spotifyPageUrl, final String imageUrl) {
            return new Artist(name, spotifyPageUrl, imageUrl);
        }

        public String getName() {
            return name;
        }

        public String getSpotifyPageUrl() {
            return spotifyPageUrl;
        }

        public String getImageUrl() {
            return imageUrl;
        }
    }
}
