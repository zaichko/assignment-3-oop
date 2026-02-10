package kz.aitu.digitalcontent.dto;

public class DigitalContentDTO {
    private int id;
    private String name;
    private int releaseYear;
    private boolean available;
    private String contentType;
    private String description;
    private String creatorCountry;
    private String creatorBio;

    private Boolean rentable;
    private Integer durationMinutes;

    private Integer trackCount;

    public DigitalContentDTO() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatorCountry() {
        return creatorCountry;
    }

    public void setCreatorCountry(String creatorCountry) {
        this.creatorCountry = creatorCountry;
    }

    public String getCreatorBio() {
        return creatorBio;
    }

    public void setCreatorBio(String creatorBio) {
        this.creatorBio = creatorBio;
    }

    public Boolean getRentable() {
        return rentable;
    }

    public void setRentable(Boolean rentable) {
        this.rentable = rentable;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public Integer getTrackCount() {
        return trackCount;
    }

    public void setTrackCount(Integer trackCount) {
        this.trackCount = trackCount;
    }
}