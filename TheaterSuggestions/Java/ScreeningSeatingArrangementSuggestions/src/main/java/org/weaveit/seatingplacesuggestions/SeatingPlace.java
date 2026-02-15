package org.weaveit.seatingplacesuggestions;

public class SeatingPlace {
    private final String rowName;
    private final int number;
    private final PricingCategory pricingCategory;
    private boolean isAvailable;

    public SeatingPlace(String rowName, int number, PricingCategory pricingCategory, boolean isAvailable) {
        this.rowName = rowName;
        this.number = number;
        this.pricingCategory = pricingCategory;
        this.isAvailable = isAvailable;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public boolean matchCategory(PricingCategory pricingCategory) {
        return this.pricingCategory == pricingCategory;
    }

    public String name() {
        return rowName + number;
    }
}
