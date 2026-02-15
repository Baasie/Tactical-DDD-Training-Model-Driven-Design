package org.weaveit.seatingplacesuggestions

class SuggestionIsMade(seatingOptionIsSuggested: SeatingOptionIsSuggested) {
    private val suggestedSeats: List<SeatingPlace> = seatingOptionIsSuggested.seats()
    private val partyRequested: Int = seatingOptionIsSuggested.partyRequested
    val pricingCategory: PricingCategory = seatingOptionIsSuggested.pricingCategory

    fun seatNames(): List<String> {
        return suggestedSeats.map { it.toString() }
    }

    fun matchExpectation(): Boolean {
        return suggestedSeats.size == partyRequested
    }
}
