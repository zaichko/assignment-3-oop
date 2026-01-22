package com.zaichko.digitalstore.model;

public class Creator extends BaseEntity implements Validate{

    private String country;
    private String bio;

    public Creator(String name, String country, String bio){
        super(name);
        this.country = country;
        this.bio = bio;
    }

    @Override
    public String describe(){
        String biography = (this.bio == null || this.bio.isBlank()) ? "No biography" : this.bio;
        return getEntityType() + " ID: " + getId()
                + "\t|\tName: " + getName()
                + "\t|\tCountry: " + this.country
                + "\t|\tBiography: " + this.bio;
    }

    @Override
    public String toString(){
        return describe();
    }

    @Override
    public String getEntityType(){
        return "Creator";
    }

    @Override
    public void validate(){
        if (this.country == null || this.country.isBlank()){
            this.country = "No data";
        }
        if (this.bio == null || this.bio.isBlank()){
            this.bio = "No biography written yet";
        }
    }

    public String getCountry(){
        return this.country;
    }

    public String getBio(){
        return this.bio;
    }

    public void setCountry(String country){
        if (country == null || country.isBlank()){
            this.country = "No data";
        } else {
            this.country = country;
        }
    }

    public void setBio(String bio){
        if (bio == null || bio.isBlank()){
            this.bio = "No biography written yet";
        } else {
            this.bio = bio;
        }
    }

}
