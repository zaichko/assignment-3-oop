package kz.aitu.digitalcontent.patterns;

import kz.aitu.digitalcontent.model.Purchase;
import java.time.LocalDate;

public class PurchaseBuilder {
    private int purchaseId;
    private int userId;
    private int contentId;
    private LocalDate purchaseDate;
    private double pricePaid;

    public PurchaseBuilder() {
        // Set defaults
        this.purchaseDate = LocalDate.now();
        this.pricePaid = 0.0;
    }

    public PurchaseBuilder purchaseId(int purchaseId) {
        this.purchaseId = purchaseId;
        return this;
    }

    public PurchaseBuilder userId(int userId) {
        this.userId = userId;
        return this;
    }

    public PurchaseBuilder contentId(int contentId) {
        this.contentId = contentId;
        return this;
    }

    public PurchaseBuilder purchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
        return this;
    }

    public PurchaseBuilder pricePaid(double pricePaid) {
        this.pricePaid = pricePaid;
        return this;
    }

    public Purchase build() {
        Purchase purchase = new Purchase(purchaseId, userId, contentId,
                purchaseDate, pricePaid);
        purchase.validate();
        return purchase;
    }

    public static PurchaseBuilder newPurchase() {
        return new PurchaseBuilder();
    }
}