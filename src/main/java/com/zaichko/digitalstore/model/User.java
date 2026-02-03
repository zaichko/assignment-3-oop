package com.zaichko.digitalstore.model;

import com.zaichko.digitalstore.exception.InvalidInputException;

public class User extends BaseEntity implements Validate{

    private String email;

    public User(String name, String email){
        super(name);
        this.email = email;

    }

    @Override
    public String describe(){
        return displayInfo()
                + "\t|\tEmail: " + getEmail();
    }

    @Override
    public String toString(){
        return describe();
    }

    @Override
    public String getEntityType(){
        return "User";
    }

    @Override
    public void validate() {
        if(getName() == null || getName().isBlank()){
            throw new InvalidInputException("User name cannot be empty");
        }

        if (getEmail() == null || getEmail().isBlank()) {
            throw new InvalidInputException("User email cannot be empty");
        }

        if (!getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new InvalidInputException("Invalid email format");
        }
    }

    public String getEmail(){
        return this.email;
    }

    public void setEmail(String email){
        this.email = (email == null) ? this.email : email.trim();
    }

}
