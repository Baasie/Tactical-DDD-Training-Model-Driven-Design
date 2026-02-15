package org.weaveit.seatingplacesuggestions

class Row(
    private val name: String,
    private val seatingPlaces: List<SeatingPlace>
) {
    fun suggestSeatingOption(partyRequested: Int, pricingCategory: PricingCategory): SeatingOptionIsSuggested {
        val seatAllocation = SeatingOptionIsSuggested(partyRequested, pricingCategory)

        for (seat in seatingPlaces) {
            if (seat.isAvailable() && seat.matchCategory(pricingCategory)) {
                seatAllocation.addSeat(seat)

                if (seatAllocation.matchExpectation()) {
                    return seatAllocation
                }
            }
        }

        return SeatingOptionIsNotAvailable(partyRequested, pricingCategory)
    }
}
