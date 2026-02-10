package kz.aitu.digitalcontent.model;

public class Creator {
    private String country;
    private String bio;

    public Creator() {
    }

    public Creator(String country, String bio) {
        this.country = country;
        this.bio = bio;
    }

    public String toString() {
        return String.format("Creator from %s: %s", country, bio);
    }

    public void validate() {
        if (country == null || country.trim().isEmpty()) {
            throw new IllegalArgumentException("Country cannot be empty");
        }
        if (bio == null || bio.trim().isEmpty()) {
            throw new IllegalArgumentException("Bio cannot be empty");
        }
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}