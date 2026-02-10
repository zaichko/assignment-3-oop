package kz.aitu.digitalcontent.model;

public abstract class DigitalContent extends BaseEntity {
    private int releaseYear;
    private boolean available;
    private Creator creator;
    private String description;

    public DigitalContent() {
        super();
    }

    public DigitalContent(int id, String name, int releaseYear, boolean available,
                          Creator creator, String description) {
        super(id, name);
        this.releaseYear = releaseYear;
        this.available = available;
        this.creator = creator;
        this.description = description;
    }

    public abstract String availabilityString();

    @Override
    public String displayInfo() {
        return String.format("%s | Released: %d | %s | Creator: %s",
                getBasicInfo(), releaseYear, availabilityString(),
                creator != null ? creator.toString() : "Unknown");
    }

    @Override
    public String describe() {
        return description != null ? description : "No description available";
    }

    public void validate() {
        if (getName() == null || getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Content name cannot be empty");
        }
        if (releaseYear < 1800 || releaseYear > 2100) {
            throw new IllegalArgumentException("Release year must be between 1800 and 2100");
        }
        if (creator != null) {
            creator.validate();
        }
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Creator getCreator() {
        return creator;
    }

    public void setCreator(Creator creator) {
        this.creator = creator;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}