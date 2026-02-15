package org.weaveit.seatingplacesuggestions

enum class PricingCategory(val value: Int) {
    FIRST(1),
    SECOND(2),
    THIRD(3);

    companion object {
        fun fromValue(value: Int): PricingCategory {
            return entries.find { it.value == value }
                ?: throw IllegalArgumentException("No category for value: $value")
        }
    }
}
