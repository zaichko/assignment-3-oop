package kz.aitu.digitalcontent.model;

public class Game extends DigitalContent {

    public Game() {
        super();
    }

    public Game(int id, String name, int releaseYear, boolean available,
                Creator creator, String description) {
        super(id, name, releaseYear, available, creator, description);
    }

    @Override
    public String getEntityType() {
        return "GAME";
    }

    @Override
    public String availabilityString() {
        return isAvailable() ? "Available for Download" : "Coming Soon";
    }

    @Override
    public String describe() {
        return "Game: " + super.describe();
    }
}