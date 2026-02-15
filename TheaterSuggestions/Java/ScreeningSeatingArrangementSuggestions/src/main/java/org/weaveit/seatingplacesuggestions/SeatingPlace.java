package org.weaveit.seatingplacesuggestions;

public record SeatingPlace(
        String rowName,
        int number,
        PricingCategory pricingCategory,
        SeatingPlaceAvailability seatingPlaceAvailability
) {
    public boolean isAvailable() {
        return seatingPlaceAvailability == SeatingPlaceAvailability.AVAILABLE;
    }

    public boolean matchCategory(PricingCategory pricingCategory) {
        if (pricingCategory == PricingCategory.MIXED) {
            return true;
        }
        return this.pricingCategory == pricingCategory;
    }

    public SeatingPlace allocate() {
        if (seatingPlaceAvailability == SeatingPlaceAvailability.AVAILABLE) {
            return new SeatingPlace(rowName, number, pricingCategory, SeatingPlaceAvailability.ALLOCATED);
        }
        return this;
    }

    public String name() {
        return rowName + number;
    }

    @Override
    public String toString() {
        return name();
    }
}
