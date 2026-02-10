package kz.aitu.digitalcontent.model;

public class MusicAlbum extends DigitalContent {
    private int countTracks;

    public MusicAlbum() {
        super();
    }

    public MusicAlbum(int id, String name, int releaseYear, boolean available,
                      Creator creator, String description, int countTracks) {
        super(id, name, releaseYear, available, creator, description);
        this.countTracks = countTracks;
    }

    @Override
    public String getEntityType() {
        return "MUSIC_ALBUM";
    }

    @Override
    public String availabilityString() {
        return isAvailable() ? "Available for Streaming" : "Coming Soon";
    }

    @Override
    public String describe() {
        return String.format("Album: %s (%d tracks)", super.describe(), countTracks);
    }

    public int getCountTracks() {
        return countTracks;
    }

    public void setCountTracks(int countTracks) {
        if (countTracks <= 0) {
            throw new IllegalArgumentException("Track count must be positive");
        }
        this.countTracks = countTracks;
    }
}