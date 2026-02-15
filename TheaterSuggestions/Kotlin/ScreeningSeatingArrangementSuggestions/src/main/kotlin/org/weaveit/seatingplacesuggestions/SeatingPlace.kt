package org.weaveit.seatingplacesuggestions

data class SeatingPlace(
    val rowName: String,
    val number: Int,
    val pricingCategory: PricingCategory,
    val seatingPlaceAvailability: SeatingPlaceAvailability
) {
    fun isAvailable(): Boolean {
        return seatingPlaceAvailability == SeatingPlaceAvailability.AVAILABLE
    }

    fun matchCategory(pricingCategory: PricingCategory): Boolean {
        if (pricingCategory == PricingCategory.MIXED) {
            return true
        }
        return this.pricingCategory == pricingCategory
    }

    fun allocate(): SeatingPlace {
        return if (seatingPlaceAvailability == SeatingPlaceAvailability.AVAILABLE) {
            copy(seatingPlaceAvailability = SeatingPlaceAvailability.ALLOCATED)
        } else {
            this
        }
    }

    fun name(): String = "$rowName$number"

    override fun toString(): String = name()
}
