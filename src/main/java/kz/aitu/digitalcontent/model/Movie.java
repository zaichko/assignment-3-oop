package kz.aitu.digitalcontent.model;

public class Movie extends DigitalContent {
    private boolean rentable;
    private int durationMinutes;

    public Movie() {
        super();
    }

    public Movie(int id, String name, int releaseYear, boolean available,
                 Creator creator, String description, boolean rentable, int durationMinutes) {
        super(id, name, releaseYear, available, creator, description);
        this.rentable = rentable;
        this.durationMinutes = durationMinutes;
    }

    @Override
    public String getEntityType() {
        return "MOVIE";
    }

    @Override
    public String availabilityString() {
        if (isAvailable()) {
            return rentable ? "Available for Rent/Purchase" : "Available for Purchase Only";
        }
        return "Not Available";
    }

    @Override
    public String describe() {
        return String.format("Movie: %s (Duration: %d minutes)",
                super.describe(), durationMinutes);
    }

    public void changeRentability() {
        this.rentable = !this.rentable;
    }

    public boolean isRentable() {
        return rentable;
    }

    public void setRentable(boolean rentable) {
        this.rentable = rentable;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }
}