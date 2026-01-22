package com.zaichko.digitalstore.model;

public class Game extends DigitalContent{

    public Game(String name, Creator creator, int releaseYear, boolean available){
        super(name, creator, releaseYear, available);
    }

    @Override
    public void validate(){
        super.validate();
    }

    @Override
    public String getEntityType(){
        return "Game";
    }

    @Override
    public double getPrice() {
        return 15.99;
    }

}
