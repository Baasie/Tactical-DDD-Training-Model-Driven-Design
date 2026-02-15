package org.weaveit.seatingplacesuggestions

data class Row private constructor(
    val name: String,
    private val _seatingPlaces: List<SeatingPlace>
) {
    val seatingPlaces: List<SeatingPlace> get() = _seatingPlaces

    companion object {
        operator fun invoke(name: String, seatingPlaces: List<SeatingPlace>): Row =
            Row(name, seatingPlaces.toList())
    }

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

    fun allocate(seatsToAllocate: List<SeatingPlace>): Row {
        val seatNamesToAllocate = seatsToAllocate.map { it.name() }.toSet()

        val updatedSeats = seatingPlaces.map { seat ->
            if (seat.name() in seatNamesToAllocate) {
                seat.allocate()
            } else {
                seat
            }
        }
        return Row(name, updatedSeats)
    }
}
