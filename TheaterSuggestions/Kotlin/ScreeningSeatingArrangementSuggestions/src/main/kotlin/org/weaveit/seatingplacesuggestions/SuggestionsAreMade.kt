package org.weaveit.seatingplacesuggestions

open class SuggestionsAreMade(
    val showId: String,
    val partyRequested: Int
) {
    private val seatsByCategory = mutableMapOf<PricingCategory, MutableList<String>>()

    fun addSeats(category: PricingCategory, seatNames: List<String>) {
        seatsByCategory.getOrPut(category) { mutableListOf() }.addAll(seatNames)
    }

    fun seatNames(pricingCategory: PricingCategory): List<String> {
        return seatsByCategory[pricingCategory] ?: emptyList()
    }
}
