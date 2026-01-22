package com.zaichko.digitalstore.model;

import com.zaichko.digitalstore.exception.DuplicateResourceException;
import com.zaichko.digitalstore.exception.InvalidInputException;

public abstract class BaseEntity {

    private int id;
    private String name;

    public BaseEntity(String name){
        if(name == null || name.isBlank()){
            throw new InvalidInputException("Name cannot be blank");
        }
        this.name = name;
    }

    public String displayInfo(){
        return id + ": " + name;
    }

    public abstract String describe();

    public abstract String getEntityType();

    public int getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public void setId(int id){
        if(this.id != 0){
            throw new DuplicateResourceException("ID is already set");
        }
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

}