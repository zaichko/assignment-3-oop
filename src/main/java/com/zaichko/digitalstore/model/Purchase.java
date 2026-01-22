package com.zaichko.digitalstore.model;

import com.zaichko.digitalstore.exception.DuplicateResourceException;
import com.zaichko.digitalstore.exception.InvalidInputException;
import java.time.LocalDate;

public class Purchase implements Validate {

    private int purchaseId;
    private final int userId;
    private final int contentId;
    private final LocalDate purchaseDate;
    private final double pricePaid;

    // New purchase
    public Purchase(int userId, int contentId, double pricePaid) {
        this.userId = userId;
        this.contentId = contentId;
        this.pricePaid = pricePaid;
        this.purchaseDate = LocalDate.now();
    }

    // Reads purchase from database
    public Purchase(int userId, int contentId, double pricePaid, LocalDate purchaseDate){
        this.userId = userId;
        this.contentId = contentId;
        this.pricePaid = pricePaid;
        this.purchaseDate = purchaseDate;
    }

    @Override
    public void validate() {
        if (userId <= 0) {
            throw new InvalidInputException("Invalid user id");
        }
        if (contentId <= 0) {
            throw new InvalidInputException("Invalid content id");
        }
        if (pricePaid <= 0) {
            throw new InvalidInputException("Price must be positive");
        }
    }

    @Override
    public String toString(){
        return "Purchase ID: " + getPurchaseId()
                + "\t|\tUser ID: " + getUserId()
                + "\t|\tContent ID: " + getContentId()
                + "\t|\tDate: " + getPurchaseDate();
    }

    public int getPurchaseId(){
        return this.purchaseId;
    }

    public int getUserId() {
        return userId;
    }

    public int getContentId() {
        return contentId;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public double getPricePaid() {
        return pricePaid;
    }

    public void setPurchaseId(int id){
        if (this.purchaseId != 0){
            throw new DuplicateResourceException("ID is already set");
        }
        this.purchaseId = id;
    }

}
