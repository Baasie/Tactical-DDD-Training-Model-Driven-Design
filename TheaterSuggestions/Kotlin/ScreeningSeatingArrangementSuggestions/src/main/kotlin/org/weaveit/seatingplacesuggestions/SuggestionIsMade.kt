package org.weaveit.seatingplacesuggestions

data class SuggestionIsMade private constructor(
    private val _suggestedSeats: List<SeatingPlace>,
    val partyRequested: Int,
    val pricingCategory: PricingCategory
) {
    val suggestedSeats: List<SeatingPlace> get() = _suggestedSeats

    companion object {
        operator fun invoke(
            suggestedSeats: List<SeatingPlace>,
            partyRequested: Int,
            pricingCategory: PricingCategory
        ): SuggestionIsMade = SuggestionIsMade(suggestedSeats.toList(), partyRequested, pricingCategory)

        operator fun invoke(seatingOption: SeatingOption): SuggestionIsMade =
            invoke(seatingOption.seats(), seatingOption.partyRequested, seatingOption.pricingCategory)
    }

    fun seatNames(): List<String> = suggestedSeats.map { it.toString() }

    fun matchExpectation(): Boolean = suggestedSeats.size == partyRequested
}
