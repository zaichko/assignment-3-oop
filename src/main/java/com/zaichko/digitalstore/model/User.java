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
        return getEntityType() + " ID: " + getId()
                + "\t|\tName: " + getName()
                + "\t|\tEmail: " + this.email;
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
        if (email == null || email.isBlank()) {
            throw new InvalidInputException("User email cannot be empty");
        }

        if (!email.contains("@")) {
            throw new InvalidInputException("Invalid email format");
        }
    }

    public String getEmail(){
        return this.email;
    }

    public void setEmail(String email){
        if(email == null){
            this.email = null;
        } else {
            this.email = email.trim();
        }
    }

}
