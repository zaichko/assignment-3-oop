package com.zaichko.digitalstore.model;

import com.zaichko.digitalstore.exception.InvalidInputException;

public abstract class DigitalContent extends BaseEntity implements PricedItem, Validate{

    private int releaseYear;
    private boolean available;
    private Creator creator;
    protected String description;

    public DigitalContent(String name, Creator creator, int releaseYear, boolean available){
        super(name);

        this.creator = creator;
        this.releaseYear = releaseYear;
        this.available = available;
    }

    @Override
    public String describe(){
        String desc = (this.description == null || this.description.isBlank()) ? "No description" : this.description;
        return getEntityType() + " ID: " + getId()
                + "\t|\tName: " + getName()
                + "\t|\tCreator: " + getCreator().getName()
                + "\t|\tRelease year: " + getReleaseYear()
                + "\t|\tAvailable: " + availabilityString()
                + "\t|\tPrice: " + getPrice()
                + "\t|\tDescription: " + desc;
    }

    @Override
    public String toString() {
        return describe();
    }

    @Override
    public String getEntityType(){
        return "DigitalContent";
    }

    @Override
    public void validate(){
        if(creator == null){
            throw new InvalidInputException("A content must have a creator");
        }
        if(!(releaseYear > 0)){
            throw new InvalidInputException("Invalid release year");
        }
    }

    public String availabilityString(){
        if (available){
            return "Yes";
        }
        return "No";
    }

    public int getReleaseYear(){
        return this.releaseYear;
    }

    public boolean isAvailable(){
        return this.available;
    }

    public Creator getCreator(){
        return this.creator;
    }

    public String getDescription(){
        return (this.description == null || this.description.isBlank()) ? "No description" : this.description;
    }

    public void changeAvailability(){
        this.available = !available;
    }

    public void setReleaseYear(int releaseYear){
        if(releaseYear <= 0){
            throw new InvalidInputException("Release year is not valid");
        }
        this.releaseYear = releaseYear;
    }

    public void setCreator(Creator creator){
        if(creator == null){
            throw new InvalidInputException("A digital content must have a creator");
        }
        this.creator = creator;
    }

    public void setDescription(String description){
        this.description = description;
    }

}
