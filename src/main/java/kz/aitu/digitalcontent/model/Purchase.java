package kz.aitu.digitalcontent.model;

import java.time.LocalDate;

public class Purchase extends BaseEntity {
    private int purchaseId;
    private int userId;
    private int contentId;
    private LocalDate purchaseDate;
    private double pricePaid;

    public Purchase() {
        super();
    }

    public Purchase(int purchaseId, int userId, int contentId,
                    LocalDate purchaseDate, double pricePaid) {
        this.purchaseId = purchaseId;
        this.userId = userId;
        this.contentId = contentId;
        this.purchaseDate = purchaseDate;
        this.pricePaid = pricePaid;
    }

    @Override
    public String getEntityType() {
        return "PURCHASE";
    }

    @Override
    public String displayInfo() {
        return String.format("Purchase #%d | User: %d | Content: %d | Date: %s | Price: $%.2f",
                purchaseId, userId, contentId, purchaseDate, pricePaid);
    }

    @Override
    public String describe() {
        return String.format("Purchase made on %s for $%.2f", purchaseDate, pricePaid);
    }

    public void validate() {
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        if (contentId <= 0) {
            throw new IllegalArgumentException("Invalid content ID");
        }
        if (pricePaid < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        if (purchaseDate == null) {
            throw new IllegalArgumentException("Purchase date cannot be null");
        }
    }

    public int getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(int purchaseId) {
        this.purchaseId = purchaseId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public double getPricePaid() {
        return pricePaid;
    }

    public void setPricePaid(double pricePaid) {
        this.pricePaid = pricePaid;
    }
}