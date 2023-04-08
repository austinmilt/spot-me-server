package com.austinmilt.spotme.spotify;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class GetArtistsResponse {
    private List<Artist> artists;

    public List<Artist> getArtists() {
        return artists;
    }

    public static class Artist {
        private Map<String, String> external_urls;
        private String href;
        private String id;
        private String name;
        private String type;
        private String uri;
        private Followers followers;
        private Set<String> genres;
        private List<Image> images;
        private int popularity;

        public Map<String, String> getExternalUrls() {
            return external_urls;
        }

        public String getHref() {
            return href;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public String getUri() {
            return uri;
        }

        public Followers getFollowers() {
            return followers;
        }

        public Set<String> getGenres() {
            return genres;
        }

        public List<Image> getImages() {
            return images;
        }

        public int getPopularity() {
            return popularity;
        }

    }

    public static class Followers {
        private String href;
        private int total;

        public String getHref() {
            return href;
        }

        public int getTotal() {
            return total;
        }

    }
}
