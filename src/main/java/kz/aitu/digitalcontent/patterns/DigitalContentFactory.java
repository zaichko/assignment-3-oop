package kz.aitu.digitalcontent.patterns;

import kz.aitu.digitalcontent.model.*;

public class DigitalContentFactory {

    public static DigitalContent createContent(String type, int id, String name,
                                               int releaseYear, boolean available,
                                               Creator creator, String description) {
        if (type == null) {
            throw new IllegalArgumentException("Content type cannot be null");
        }

        switch (type.toUpperCase()) {
            case "GAME":
                return new Game(id, name, releaseYear, available, creator, description);

            case "MOVIE":
                // Default values for movie-specific fields
                return new Movie(id, name, releaseYear, available, creator, description,
                        true, 120);

            case "MUSIC_ALBUM":
            case "ALBUM":
                // Default track count
                return new MusicAlbum(id, name, releaseYear, available, creator, description, 10);

            default:
                throw new IllegalArgumentException("Unknown content type: " + type);
        }
    }

    public static DigitalContent createContent(String type, String name) {
        Creator defaultCreator = new Creator("Unknown", "No bio available");
        return createContent(type, 0, name, 2024, true, defaultCreator, "No description");
    }

    public static Movie createMovie(int id, String name, int releaseYear, boolean available,
                                    Creator creator, String description,
                                    boolean rentable, int duration) {
        return new Movie(id, name, releaseYear, available, creator, description,
                rentable, duration);
    }

    public static MusicAlbum createMusicAlbum(int id, String name, int releaseYear,
                                              boolean available, Creator creator,
                                              String description, int trackCount) {
        return new MusicAlbum(id, name, releaseYear, available, creator, description, trackCount);
    }
}