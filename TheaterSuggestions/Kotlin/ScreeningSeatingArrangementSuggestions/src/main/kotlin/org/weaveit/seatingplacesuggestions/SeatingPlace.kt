package org.weaveit.seatingplacesuggestions

class SeatingPlace(
    private val rowName: String,
    private val number: Int,
    private val pricingCategory: PricingCategory,
    private var seatingPlaceAvailability: SeatingPlaceAvailability
) {
    fun isAvailable(): Boolean {
        return seatingPlaceAvailability == SeatingPlaceAvailability.AVAILABLE
    }

    fun matchCategory(pricingCategory: PricingCategory): Boolean {
        return this.pricingCategory == pricingCategory
    }

    fun allocate() {
        if (seatingPlaceAvailability == SeatingPlaceAvailability.AVAILABLE) {
            seatingPlaceAvailability = SeatingPlaceAvailability.ALLOCATED
        }
    }

    override fun toString(): String {
        return "$rowName$number"
    }
}
