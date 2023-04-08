package com.austinmilt.spotme.spotify;

import java.util.List;
import java.util.Map;

public class SavedTracksResponse {
    private List<SavedTracksItem> items;
    private int total;
    private int limit;
    private int offset;
    private String href;
    private String next;
    private String previous;

    public List<SavedTracksItem> getItems() {
        return items;
    }

    public int getTotal() {
        return total;
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }

    public String getHref() {
        return href;
    }

    public String getNext() {
        return next;
    }

    public String getPrevious() {
        return previous;
    }

    public static class SavedTracksItem {
        // TODO convert fields to better types like Date, etc.
        private String added_at;
        private Track track;

        public String getAddedAt() {
            return added_at;
        }

        public Track getTrack() {
            return track;
        }
    }

    public static class Track {
        private Album album;
        private List<Artist> artists;
        private List<String> available_markets;
        private int disc_number;
        private int duration_ms;
        private boolean explicit;
        private Map<String, String> external_ids;
        private Map<String, String> external_urls;
        private String href;
        private String id;
        private boolean is_local;
        private String name;
        private int popularity;
        private String preview_url;
        private int track_number;
        private String type;
        private String uri;

        public Album getAlbum() {
            return album;
        }

        public List<Artist> getArtists() {
            return artists;
        }

        public List<String> getAvailableMarkets() {
            return available_markets;
        }

        public int getDiscNumber() {
            return disc_number;
        }

        public int getDurationMs() {
            return duration_ms;
        }

        public boolean isExplicit() {
            return explicit;
        }

        public Map<String, String> getExternalIds() {
            return external_ids;
        }

        public Map<String, String> getExternalUrls() {
            return external_urls;
        }

        public String getHref() {
            return href;
        }

        public String getId() {
            return id;
        }

        public boolean isIsLocal() {
            return is_local;
        }

        public String getName() {
            return name;
        }

        public int getPopularity() {
            return popularity;
        }

        public String getPreviewUrl() {
            return preview_url;
        }

        public int getTrackNumber() {
            return track_number;
        }

        public String getType() {
            return type;
        }

        public String getUri() {
            return uri;
        }
    }

    public static class Album {
        private String album_group;
        private String album_type;
        private List<Artist> artists;
        private List<String> available_markets;
        private Map<String, String> external_urls;
        private String href;
        private String id;
        private List<Image> images;
        private String name;
        private String release_date;
        private String release_date_precision;
        private int total_tracks;
        private String type;
        private String uri;

        public String getAlbumGroup() {
            return album_group;
        }

        public String getAlbumType() {
            return album_type;
        }

        public List<Artist> getArtists() {
            return artists;
        }

        public List<String> getAvailableMarkets() {
            return available_markets;
        }

        public Map<String, String> getExternalUrls() {
            return external_urls;
        }

        public String getHref() {
            return href;
        }

        public String getId() {
            return id;
        }

        public List<Image> getImages() {
            return images;
        }

        public String getName() {
            return name;
        }

        public String getReleaseDate() {
            return release_date;
        }

        public String getReleaseDatePrecision() {
            return release_date_precision;
        }

        public int getTotalTracks() {
            return total_tracks;
        }

        public String getType() {
            return type;
        }

        public String getUri() {
            return uri;
        }

    }

    public static class Artist {
        private Map<String, String> external_urls;
        private String href;
        private String id;
        private String name;
        private String type;
        private String uri;

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

    }
}
