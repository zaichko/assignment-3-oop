package com.zaichko.digitalstore.model;

import com.zaichko.digitalstore.exception.InvalidInputException;

public class MusicAlbum extends DigitalContent{

    private int countTracks;

    public MusicAlbum(String name, Creator creator, int releaseYear, boolean available, int countTracks){
        super(name, creator, releaseYear, available);
        this.countTracks = countTracks;
    }

    @Override
    public String getEntityType(){
        return "MusicAlbum";
    }

    @Override
    public double getPrice(){
        return 1.99 * this.countTracks;
    }

    @Override
    public void validate(){
        super.validate();
        validateTrackCount(this.countTracks);
    }

    private void validateTrackCount(int countTracks){
        if(countTracks <= 0){
            throw new InvalidInputException("An album must have at least one track");
        }
    }

    public int getCountTracks(){
        return this.countTracks;
    }

    public void setCountTracks(int countTracks){
        validateTrackCount(countTracks);
        this.countTracks = countTracks;
    }

}
