package com.zaichko.digitalstore.model;

import com.zaichko.digitalstore.exception.InvalidInputException;

public class Movie extends DigitalContent{

    private boolean rentable;
    private int durationMinutes;

    public Movie(String name, Creator creator, int releaseYear, boolean available, boolean rentable, int durationMinutes){
        super(name, creator, releaseYear, available);

        this.rentable = rentable;
        this.durationMinutes = durationMinutes;
    }

    public String describe(){
        return displayInfo()
                + "\t|\tCreator: " + getCreator().getName()
                + "\t|\tRelease year: " + getReleaseYear()
                + "\t|\tDuration in minutes: " + getDurationMinutes()
                + "\t|\tRentable: " + rentabilityToString()
                + "\t|\tAvailable: " + availabilityString()
                + "\t|\tPrice: " + getPrice()
                + "\t|\tDescription: " + getDescription();
    }

    @Override
    public String getContentType(){
        return "Movie";
    }

    @Override
    public double getPrice(){
        return (this.rentable) ? 4.99 : 25.00;
    }

    @Override
    public void validate(){
        super.validate();
        if(durationMinutes <= 0){
            throw new InvalidInputException("Invalid movie duration in minutes");
        }
    }

    public void changeRentability(){
        this.rentable = !rentable;
    }

    public void setDurationMinutes(int minutes){
        if(minutes <= 0){
            throw new InvalidInputException("Invalid movie duration in minutes");
        }
        this.durationMinutes = minutes;
    }

    public boolean getRentability(){
        return this.rentable;
    }

    public String rentabilityToString(){
        return (rentable) ? "Yes" : "No";
    }

    public int getDurationMinutes(){
        return this.durationMinutes;
    }

}
