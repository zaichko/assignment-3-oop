package com.zaichko.digitalstore.model;

import com.zaichko.digitalstore.exception.InvalidInputException;

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
        return displayInfo()
                + "\t|\tCountry: " + getCountry()
                + "\t|\tBiography: " + getBio();
    }

    @Override
    public void validate(){
        if(getName() == null || getName().isBlank()){
            throw new InvalidInputException("Creator name cannot be empty");
        }
    }

    @Override
    public String toString(){
        return describe();
    }

    @Override
    public String getEntityType(){
        return "Creator";
    }

    public String getCountry(){
        return this.country;
    }

    public String getBio(){
        return (this.bio == null || this.bio.isBlank()) ? "No biography" : this.bio;
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
