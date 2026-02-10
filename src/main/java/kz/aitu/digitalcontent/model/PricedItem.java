package kz.aitu.digitalcontent.model;

public interface PricedItem {

    double getPrice();

    default String getFormattedPrice() {
        return String.format("$%.2f", getPrice());
    }

    static double calculateDiscount(double originalPrice, double discountPercent) {
        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException("Discount must be between 0 and 100");
        }
        return originalPrice * (1 - discountPercent / 100);
    }
}