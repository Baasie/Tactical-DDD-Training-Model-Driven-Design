package org.weaveit.seatingplacesuggestions;

public enum PricingCategory {
    FIRST(1),
    SECOND(2),
    THIRD(3);

    private final int value;

    PricingCategory(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static PricingCategory valueOf(int value) {
        for (var category : values()) {
            if (category.value == value) {
                return category;
            }
        }
        throw new IllegalArgumentException("No category for value: " + value);
    }
}
