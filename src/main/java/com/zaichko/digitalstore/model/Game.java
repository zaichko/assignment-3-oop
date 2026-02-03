package com.zaichko.digitalstore.model;

public class Game extends DigitalContent{

    public Game(String name, Creator creator, int releaseYear, boolean available){
        super(name, creator, releaseYear, available);
    }

    @Override
    public String describe(){
        return displayInfo()
                + "\t|\tCreator: " + getCreator().getName()
                + "\t|\tRelease year: " + getReleaseYear()
                + "\t|\tAvailable: " + availabilityString()
                + "\t|\tPrice: " + getPrice()
                + "\t|\tDescription: " + getDescription();
    }

    @Override
    public String getContentType(){
        return "Game";
    }

    @Override
    public double getPrice() {
        return 15.99;
    }

}
